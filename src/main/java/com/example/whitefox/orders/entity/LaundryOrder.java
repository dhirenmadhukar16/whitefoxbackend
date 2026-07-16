package com.example.whitefox.orders.entity;



import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.orders.enums.PaymentStatus;
import com.example.whitefox.orders.enums.DeliveryType;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import com.example.whitefox.riders.entity.Rider;

@Entity
@Table(name = "laundry_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LaundryOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String orderNumber;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private String paymentMethod;

    private Double subtotal;

    private Double gst;

    private Double totalAmount;

    private LocalDate pickupDate;

    private LocalDate deliveryDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Double paidAmount;

    private Double remainingAmount;
    @ManyToOne
    @JoinColumn(name = "delivery_rider_id")
    private Rider deliveryRider;

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    private String pickupType;

    private String deliveryOtp;

    private String pickupOtp;

    @PrePersist
    public void prePersist() {
        status = OrderStatus.CREATED;
        paymentStatus = PaymentStatus.UNPAID;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        paidAmount = 0.0;
        remainingAmount = totalAmount != null ? totalAmount : 0.0;
        if (deliveryType == null) {
            deliveryType = DeliveryType.RIDER_DELIVERY;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}