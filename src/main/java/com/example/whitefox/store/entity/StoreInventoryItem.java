package com.example.whitefox.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreInventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    private String itemName;
    
    private String category; // Detergent, Packaging, Equipment

    private Integer quantity;

    private Integer minimumThreshold;

    private String unit; // kg, liters, pieces

    private LocalDateTime lastRestocked;
    
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.lastRestocked == null) {
            this.lastRestocked = LocalDateTime.now();
        }
    }
}
