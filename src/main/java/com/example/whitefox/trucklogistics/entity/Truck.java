package com.example.whitefox.trucklogistics.entity;

import com.example.whitefox.trucklogistics.enums.TruckStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "trucks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String truckNumber;

    private String driverName;

    private String driverPhone;

    private Double capacityKg;

    private Double currentLatitude;

    private Double currentLongitude;

    @Enumerated(EnumType.STRING)
    private TruckStatus status;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        status = TruckStatus.AVAILABLE;
        active = true;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}