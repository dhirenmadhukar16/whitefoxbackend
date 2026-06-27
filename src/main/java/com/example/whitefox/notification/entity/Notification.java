package com.example.whitefox.notification.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String title;

    @Column(length = 5000)
    private String message;

    /**
     * ADMIN
     * STORE
     * RIDER
     * CUSTOMER
     */
    private String senderType;

    private UUID senderId;

    /**
     * ADMIN
     * STORE
     * RIDER
     * CUSTOMER
     * ALL
     */
    private String receiverType;

    private UUID receiverId;

    /**
     * INFO
     * SUCCESS
     * WARNING
     * ERROR
     */
    private String notificationType;

    private Boolean isRead;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        isRead = false;
    }
}