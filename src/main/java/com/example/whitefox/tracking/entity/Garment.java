package com.example.whitefox.tracking.entity;



import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.tracking.enums.GarmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "garments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Garment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private LaundryOrder order;

    @Column(unique = true, nullable = false)
    private String storeQrCode;

    @Column(unique = true)
    private String outingQrCode;

    private String itemName;

    private String serviceType;

    private String color;

    private String stains;

    private String specialInstructions;

    @ElementCollection
    @CollectionTable(name = "garment_photos", joinColumns = @JoinColumn(name = "garment_id"))
    @Column(name = "photo_url")
    private java.util.List<String> photoUrls;

    @Enumerated(EnumType.STRING)
    private GarmentStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_bag_id")
    private Bag currentBag;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        status = GarmentStatus.TAGGED_AT_STORE;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}