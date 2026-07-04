package com.example.whitefox.customerreview.entity;



import com.example.whitefox.customers.entity.Customer;
import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.riders.entity.Rider;
import com.example.whitefox.store.entity.Store;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customer_reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerReview {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private LaundryOrder order;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "rider_id")
    private Rider rider;

    private Integer storeRating;

    private Integer riderRating;

    private Integer overallRating;

    @Column(length = 2000)
    private String feedback;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
