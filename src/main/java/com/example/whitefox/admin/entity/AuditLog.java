package com.example.whitefox.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "AdminPanelAuditLog")
@Table(name = "admin_audit_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String actorName; // Name of Admin, Store Staff, Customer, etc.

    private String actorType; // ADMIN, STORE, CUSTOMER, SYSTEM

    private String action; // e.g., "Deleted Store", "Updated Pricing"

    private String entityType; // "Store", "PricingRule", "User"
    
    private String entityId; // The ID of the entity that was affected

    private String ipAddress;

    private LocalDateTime timestamp;

    @PrePersist
    public void prePersist() {
        timestamp = LocalDateTime.now();
    }
}
