package com.example.whitefox.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "admin_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminRole {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String roleName;

    private String description;

    private Boolean canManageStores;
    private Boolean canManageRiders;
    private Boolean canManageOrders;
    private Boolean canManageSettings;
    
    private Boolean isActive;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
    }
}
