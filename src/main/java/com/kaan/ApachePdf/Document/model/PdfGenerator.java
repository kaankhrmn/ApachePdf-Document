package com.kaan.ApachePdf.Document.model;

import org.springframework.http.ResponseEntity;

public interface PdfGenerator {

    ResponseEntity<byte[]> generateKisiKarti(String ad_soyad, String adres, String dogumYeri, String tckn, String cinsiyet);

    ResponseEntity<byte[]> generateBasariBelgesi(String ad_soyad, String tarih);
}
