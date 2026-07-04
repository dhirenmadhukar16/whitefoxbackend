package com.example.whitefox.notification.service;

import com.example.whitefox.notification.dto.CreateNotificationRequest;
import com.example.whitefox.notification.dto.NotificationResponse;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    NotificationResponse createNotification(CreateNotificationRequest request);

    List<NotificationResponse> getAllNotifications();

    List<NotificationResponse> getByReceiver(String receiverType, UUID receiverId);

    NotificationResponse markAsRead(UUID notificationId);
}