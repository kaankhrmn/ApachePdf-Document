package com.kaan.ApachePdf.Document.service.Impl;

import com.kaan.ApachePdf.Document.qrCodeGenerator.QrCodeGenerator;
import com.kaan.ApachePdf.Document.model.PdfFactory;
import com.kaan.ApachePdf.Document.model.PdfGenerator;
import com.kaan.ApachePdf.Document.service.BasariBelgesiService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Date;

@Service
public class BasariBelgesiImpl implements PdfGenerator, BasariBelgesiService {

    @Override
    public ResponseEntity<byte[]> generateDocument(String type, String ad_soyad, String tarih) {
        try {
            PdfGenerator pdfGenerator = PdfFactory.pdfPattern(type);
            return pdfGenerator.generateBasariBelgesi(ad_soyad, tarih);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @Override
    public ResponseEntity<byte[]> generateBasariBelgesi(String ad_soyad, String tarih) {
        try {
            ClassPathResource pdftemplate = new ClassPathResource("templates/BasariBelgesi.pdf");
            InputStream inputStream = pdftemplate.getInputStream();
            PDDocument document = PDDocument.load(inputStream);
            PDPage page = document.getPage(0);

            PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            File fontFile = new File("src/main/resources/templates/arial.ttf");
            PDType0Font font = PDType0Font.load(document, fontFile);
            contentStream.setFont(font, 40);
            contentStream.setNonStrokingColor(Color.BLACK);

            contentStream.beginText();
            contentStream.newLineAtOffset(370, 250);
            contentStream.showText(ad_soyad);
            contentStream.endText();

            contentStream.beginText();
            contentStream.newLineAtOffset(450, 450);
            contentStream.showText(tarih);
            contentStream.endText();


            //QR KOD OLUŞTUR VE PDF'YE EKLE
            String belge_sahibi = "Kaan Kahraman";
            String belge_tarihi = new Date().toString();
            String belgeBilgisi = String.format("Bu belge %s tarihinde %s tarafindan üretilmistir", belge_tarihi, belge_sahibi);
            String  belgeB = "http://localhost:8080/belge/goster/1345"; //adrese gönderip doğrulama yaptır
            //postgreSql
            //belgeyi indirince mail olarak atsın

            PDImageXObject qrImage = QrCodeGenerator.createQrCodeImage(document, belgeBilgisi, 100, 100);
            PDPageContentStream qrContentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true);
            qrContentStream.drawImage(qrImage, 70, 70, 100, 100);
            qrContentStream.close();

            contentStream.close();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            document.close();

            // PDF dosyasını indirme başlığıyla yanıt olarak gönder
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=output.pdf");
            headers.add("Content-Type", "application/pdf");

            System.out.printf("Basari Belgesi : %s için %s tarihinde başarıyla oluşturulmuştur.%n", ad_soyad, new Date());
            return new ResponseEntity<>(baos.toByteArray(), headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @Override
    public ResponseEntity<byte[]> generateKisiKarti(String ad_soyad, String adres, String dogumYeri, String tckn, String cinsiyet) {
        return null;
    }
}
