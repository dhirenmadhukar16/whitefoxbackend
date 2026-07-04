package com.example.whitefox.trucklogistics.entity;

import com.example.whitefox.store.entity.Store;
import com.example.whitefox.tracking.entity.Garment;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "truck_manifest_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TruckManifestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manifest_id", nullable = false)
    private TruckManifest manifest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garment_id")
    private Garment garment;

    private String qrCode;

    /**
     * LOADED
     * DELIVERED
     * MISSING
     * DAMAGED
     */
    private String status;

    private LocalDateTime loadedAt;
    // TruckManifestItem
//    private Store destinationStore;
    private LocalDateTime deliveredAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_store_id")
    private Store destinationStore;
    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = "LOADED";
        }

        if (loadedAt == null) {
            loadedAt = LocalDateTime.now();
        }
    }
}