package com.kaan.ApachePdf.Document.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "belge_dogrulama")
public class Belge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String belgeNo;

    private String adSoyad;

    private String email;

    private String belgeTuru; // Başarı Belgesi / Kişi Kartı

    private LocalDateTime indirmeTarihi;

    private boolean dogrulandi;
}