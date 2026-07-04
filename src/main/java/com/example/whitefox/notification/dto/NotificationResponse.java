package com.example.whitefox.notification.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class NotificationResponse {

    private UUID id;

    private String title;

    private String message;

    private String senderType;

    private UUID senderId;

    private String receiverType;

    private UUID receiverId;

    private String notificationType;

    private Boolean isRead;

    private LocalDateTime createdAt;
}