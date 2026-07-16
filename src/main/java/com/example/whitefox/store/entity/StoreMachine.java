package com.example.whitefox.store.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_machines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreMachine {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String machineType;

    @Column(nullable = false)
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = "ACTIVE";
        }
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
