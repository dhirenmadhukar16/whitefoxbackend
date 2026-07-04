package com.example.whitefox.customerupdates.entity;



import com.example.whitefox.customerupdates.enums.CustomerUpdateType;
import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.orders.entity.LaundryOrder;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_updates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private LaundryOrder order;

    @Enumerated(EnumType.STRING)
    private CustomerUpdateType updateType;

    @Column(length = 1000)
    private String title;

    @Column(length = 5000)
    private String description;

    private Boolean readStatus;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();

        if (readStatus == null) {
            readStatus = false;
        }
    }
}