package com.example.whitefox.ordertimeline.entity;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_timeline")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderTimeline {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private LaundryOrder order;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private String action;

    private String remarks;

    private UUID userId;

    private String userName;

    /**
     * CUSTOMER
     * STORE
     * RIDER
     * HQ
     * TRUCK
     * ADMIN
     */
    private String module;

    private LocalDateTime actionTime;
}