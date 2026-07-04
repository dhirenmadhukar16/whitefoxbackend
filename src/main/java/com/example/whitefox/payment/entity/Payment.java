package com.example.whitefox.payment.entity;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.payment.enums.PaymentMode;
import com.example.whitefox.payment.enums.PaymentTransactionStatus;
import com.example.whitefox.store.entity.Store;
//import com.example.whitefox.stores.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private LaundryOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Enumerated(EnumType.STRING)
    private PaymentMode paymentMode;

    @Enumerated(EnumType.STRING)
    private PaymentTransactionStatus status;

    @Column(nullable = false)
    private Double amount;

    @Column(length = 200)
    private String transactionReference;

    @Column(length = 500)
    private String remarks;

    private LocalDateTime paidAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (paidAt == null) {
            paidAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}