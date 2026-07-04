package com.example.whitefox.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_finance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreFinanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private LocalDate recordDate;

    private Double totalRevenue;
    
    private Double totalExpenses;
    
    private Double pendingBills;
    
    private Integer totalOrders;

    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
