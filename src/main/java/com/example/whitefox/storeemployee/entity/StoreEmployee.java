package com.example.whitefox.storeemployee.entity;



import com.example.whitefox.store.entity.Store;
import com.example.whitefox.storeemployee.enums.StoreEmployeeRole;
import com.example.whitefox.storeemployee.enums.StoreEmployeeStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreEmployee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    private StoreEmployeeRole role;

    @Enumerated(EnumType.STRING)
    private StoreEmployeeStatus status;

    private Boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        status = StoreEmployeeStatus.ACTIVE;
        active = true;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
