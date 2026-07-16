package com.example.whitefox.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "AdminStoreExpense")
@Table(name = "store_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "store_id")
    private UUID storeId;

    private String category; // RENT, ELECTRICITY, SALARY, OTHER

    private Double amount;

    private String description;

    private LocalDateTime expenseDate;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (expenseDate == null) expenseDate = LocalDateTime.now();
    }
}
