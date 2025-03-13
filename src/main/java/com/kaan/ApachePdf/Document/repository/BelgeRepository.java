package com.kaan.ApachePdf.Document.repository;

import com.kaan.ApachePdf.Document.model.Belge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BelgeRepository extends JpaRepository<Belge, Long> {

    Optional<Belge> findByBelgeNo(String belgeNo); // QR kod doğrulama için sorgu
}
