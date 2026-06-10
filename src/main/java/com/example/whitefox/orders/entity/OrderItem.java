package com.example.whitefox.orders.entity;



import com.example.whitefox.garments.entity.ServiceCatalog;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private LaundryOrder order;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private ServiceCatalog serviceCatalog;

    private String serviceType; 

    private String itemName;

    private Integer quantity;

    private Double unitPrice;

    private Double totalPrice;
}