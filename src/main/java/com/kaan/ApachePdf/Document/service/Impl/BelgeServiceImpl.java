package com.kaan.ApachePdf.Document.service.Impl;

import com.kaan.ApachePdf.Document.model.Belge;
import com.kaan.ApachePdf.Document.model.PdfFactory;
import com.kaan.ApachePdf.Document.model.PdfGenerator;
import com.kaan.ApachePdf.Document.repository.BelgeRepository;
import com.kaan.ApachePdf.Document.service.BelgeService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BelgeServiceImpl implements BelgeService {

    private static final Logger logger = LoggerFactory.getLogger(BelgeServiceImpl.class);
    private final BelgeRepository belgeRepository;
    private final MailServiceImpl mailService;

    @Override
    public Belge belgeKaydet(String adSoyad, String email, String belgeTuru) throws MessagingException {
        String belgeNo = UUID.randomUUID().toString();

        Belge belge = Belge.builder()
                .belgeNo(belgeNo)
                .belgeTuru(belgeTuru)
                .adSoyad(adSoyad)
                .email(email)
                .indirmeTarihi(LocalDateTime.now())
                .dogrulandi(false)
                .build();

        Belge savedBelge = belgeRepository.save(belge);

        // **Doğrulama Linki Gönderme**
        String verificationLink = "http://localhost:8080/api/pdf/generate/verify?belgeNo=" + belgeNo; //ngrok http 8080
        mailService.sendVerificationEmail(email, verificationLink);

        return savedBelge;
    }

    @Override
    public boolean belgeDogrula(String belgeNo) {
        Optional<Belge> belgeOpt = belgeRepository.findByBelgeNo(belgeNo);

        if (belgeOpt.isEmpty()) {
            logger.warn("Belge bulunamadı: {}", belgeNo);
            return false;
        }

        Belge mevcutBelge = belgeOpt.get();

        if (!mevcutBelge.isDogrulandi()) {
            mevcutBelge.setDogrulandi(true);
            belgeRepository.save(mevcutBelge);
        }

        logger.info("Belge doğrulandı: {}", belgeNo);
        return true;
    }

    public ResponseEntity<byte[]> belgeIndir(String belgeNo) {
        Optional<Belge> belgeOpt = belgeRepository.findByBelgeNo(belgeNo);

        if (belgeOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Belge bulunamadı.".getBytes());
        }

        Belge belge = belgeOpt.get();
        belge.setDogrulandi(true);
        belgeRepository.save(belge);

        // **Factory Pattern ile PDF oluşturma**
        PdfGenerator pdfGenerator = PdfFactory.pdfPattern(belge.getBelgeTuru());

        try {
            if ("BasariBelgesi".equalsIgnoreCase(belge.getBelgeTuru())) {
                return pdfGenerator.generateBasariBelgesi(belge.getAdSoyad(), LocalDate.now().toString());
            } else if ("KisiKarti".equalsIgnoreCase(belge.getBelgeTuru())) {
                return pdfGenerator.generateKisiKarti(belge.getAdSoyad(), "Bandırma", "Bandırma", "123456789", "Erkek");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Geçersiz belge türü.".getBytes());
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
