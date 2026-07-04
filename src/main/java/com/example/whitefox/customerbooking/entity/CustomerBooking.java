package com.example.whitefox.customerbooking.entity;



import com.example.whitefox.customerbooking.enums.CustomerBookingStatus;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "assigned_rider_id")
    private Rider assignedRider;

    private String pickupAddress;

    private LocalDate pickupDate;

    private String pickupTimeSlot;

    private String specialInstructions;

    private Double estimatedAmount;

    @Enumerated(EnumType.STRING)
    private CustomerBookingStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        status = CustomerBookingStatus.REQUESTED;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
