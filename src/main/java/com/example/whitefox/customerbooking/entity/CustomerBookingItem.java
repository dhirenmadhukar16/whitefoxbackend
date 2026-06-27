package com.example.whitefox.customerbooking.entity;



import com.example.whitefox.garments.entity.ServiceCatalog;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "customer_booking_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerBookingItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private CustomerBooking booking;

    @ManyToOne
    @JoinColumn(name = "catalog_id")
    private ServiceCatalog serviceCatalog;

    private String serviceType;

    private String itemName;

    private Integer quantity;

    private Double unitPrice;

    private Double estimatedPrice;
}
