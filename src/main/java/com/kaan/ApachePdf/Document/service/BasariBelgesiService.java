package com.kaan.ApachePdf.Document.service;

import org.springframework.http.ResponseEntity;

public interface BasariBelgesiService {
    ResponseEntity<byte[]> generateDocument(String type,String ad_soyad,String tarih);
}
