package com.example.whitefox.tracking.entity;



import com.example.whitefox.tracking.enums.GarmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "garment_tracking_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GarmentTrackingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "garment_id", nullable = false)
    private Garment garment;

    @Enumerated(EnumType.STRING)
    private GarmentStatus status;

    private String remarks;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}