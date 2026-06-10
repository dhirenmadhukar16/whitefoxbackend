package com.example.whitefox.pickupbill.entity;



import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.store.entity.Store;
import com.example.whitefox.pickupbill.enums.PickupBillStatus;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pickup_bills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickupBill {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String billNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider rider;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    private Double subtotal;

    private Double gst;

    private Double totalAmount;

    private String pickupAddress;

    @Enumerated(EnumType.STRING)
    private PickupBillStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if(status == null){
            status = PickupBillStatus.CREATED_BY_RIDER;
        }

        if(billNumber == null){
            billNumber = "PB-" + System.currentTimeMillis();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
