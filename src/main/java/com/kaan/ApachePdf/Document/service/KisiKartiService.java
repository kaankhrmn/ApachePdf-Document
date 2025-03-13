package com.kaan.ApachePdf.Document.service;

import org.springframework.http.ResponseEntity;

public interface KisiKartiService {

    ResponseEntity<byte[]> generateDocument(String type,  String ad_soyad, String adres, String dogumYeri, String tckn, String cinsiyet);
}
