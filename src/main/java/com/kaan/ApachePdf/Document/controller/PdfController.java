package com.kaan.ApachePdf.Document.controller;

import com.kaan.ApachePdf.Document.service.Impl.BelgeServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/pdf/generate")
@RequiredArgsConstructor
public class PdfController {

    private final BelgeServiceImpl belgeService;

    @PostMapping("/BasariBelgesi")
    public ResponseEntity<Void> generateBasariBelgesi(
            @RequestParam String ad_soyad,
            @RequestParam String email
    ) {
        try {
            belgeService.belgeKaydet(ad_soyad, email, "BasariBelgesi");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping("/KisiKarti")
    public ResponseEntity<Void> generateKisiKarti(
            @RequestParam String ad_soyad,
            @RequestParam String email
    ) {
        try {
            belgeService.belgeKaydet(ad_soyad, email, "KisiKarti");
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<byte[]> verifyAndDownload(@RequestParam String belgeNo) {
        return belgeService.belgeIndir(belgeNo);
    }
}
