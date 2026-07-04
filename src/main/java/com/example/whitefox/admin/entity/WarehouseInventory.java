package com.example.whitefox.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "warehouse_inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WarehouseInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String itemName; // Detergent, Packaging, Spare Parts

    private String unit; // L, Boxes, Pcs

    private Double currentStock;

    private Double minimumThreshold;
}
