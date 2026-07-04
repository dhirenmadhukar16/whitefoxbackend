package com.example.whitefox.trucklogistics.entity;

import com.example.whitefox.store.entity.Store;
import com.example.whitefox.trucklogistics.enums.TruckManifestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "truck_manifests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckManifest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private TruckTrip trip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_store_id")
    private Store sourceStore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_store_id")
    private Store destinationStore;

    /**
     * STORE_TO_HQ
     * HQ_TO_STORE
     */
    private String movementType;

    private String manifestNumber;

    private Integer totalGarments;

    private Double totalWeight;

    @Enumerated(EnumType.STRING)
    private TruckManifestStatus status;

    private LocalDateTime loadedAt;

    private LocalDateTime deliveredAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (status == null) {
            status = TruckManifestStatus.CREATED;
        }

        if (totalGarments == null) {
            totalGarments = 0;
        }

        if (totalWeight == null) {
            totalWeight = 0.0;
        }
    }
}