package com.example.whitefox.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "company_expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String category; // MARKETING, IT_INFRASTRUCTURE, LEGAL, MISC

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
