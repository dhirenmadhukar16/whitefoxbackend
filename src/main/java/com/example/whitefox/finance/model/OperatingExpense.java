package com.example.whitefox.finance.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "operating_expenses")
@Data
public class OperatingExpense {

    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    private String category;
    private Double amount;
    
    @Column(name = "expense_date")
    private LocalDateTime expenseDate;

    private String description;

    @PrePersist
    protected void onCreate() {
        if (expenseDate == null) {
            expenseDate = LocalDateTime.now();
        }
    }
}
