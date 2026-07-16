package com.example.whitefox.garments.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "service_catalog")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceCatalog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ServiceCategory category;

    @Column(length = 1000)
    private String thumbnailUrl;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Double price;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        active = true;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}