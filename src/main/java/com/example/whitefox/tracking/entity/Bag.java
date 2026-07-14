package com.example.whitefox.tracking.entity;

import com.example.whitefox.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "bags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bag {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String qrCode;

    // The store where this bag was created/packed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_store_id")
    private Store sourceStore;

    // Optional: if it's heading back to a store
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_store_id")
    private Store destinationStore;

    // CREATED, IN_TRANSIT_TO_HQ, RECEIVED_AT_HQ, PROCESSING, PACKED_FOR_STORE, IN_TRANSIT_TO_STORE, UNPACKED_AT_STORE
    private String status;

    @OneToMany(mappedBy = "currentBag", fetch = FetchType.LAZY)
    private List<Garment> garments;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
