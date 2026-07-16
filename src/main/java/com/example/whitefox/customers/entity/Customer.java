package com.example.whitefox.customers.entity;

//package com.example.whitefox.customer.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false)
    private String customerCode;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String phone;

    private String email;

    private String whatsappNumber;

    private String address;

    private String city;

    private Boolean active;

    @Builder.Default
    private Integer loyaltyPoints = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist() {
        active = true;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}