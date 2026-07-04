package com.example.whitefox.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "system_reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemReport {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String reportName;

    private String reportType; // PDF, CSV, EXCEL

    private String generatedBy;

    private String status; // GENERATING, COMPLETED, FAILED

    private String downloadUrl;

    private LocalDateTime generatedAt;

    @PrePersist
    public void prePersist() {
        generatedAt = LocalDateTime.now();
        if (status == null) status = "GENERATING";
    }
}
