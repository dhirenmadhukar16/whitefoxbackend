package com.example.whitefox.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "support_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID customerId;
    private UUID storeId;
    private UUID orderId; // Optional link to specific order

    private String customerName;

    private String issueCategory; // e.g., "Delivery Delay", "Damaged Item", "App Issue"

    private String description;

    private String status; // "OPEN", "IN_PROGRESS", "RESOLVED"

    private String priority; // "LOW", "MEDIUM", "HIGH"

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = "OPEN";
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
