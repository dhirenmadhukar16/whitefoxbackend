package com.example.whitefox.notification.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateNotificationRequest {

    private String title;

    private String message;

    private String senderType;

    private UUID senderId;

    private String receiverType;

    private UUID receiverId;

    private String notificationType;
}