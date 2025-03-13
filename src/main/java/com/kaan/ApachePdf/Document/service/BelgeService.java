package com.kaan.ApachePdf.Document.service;

import com.kaan.ApachePdf.Document.model.Belge;
import jakarta.mail.MessagingException;

public interface BelgeService {

    Belge belgeKaydet(String adSoyad, String email, String belgeTuru) throws MessagingException; ;

    boolean belgeDogrula(String belgeNo);
}
