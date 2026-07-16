package com.example.whitefox.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private LocalDateTime date;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (date == null) {
            date = LocalDateTime.now();
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
