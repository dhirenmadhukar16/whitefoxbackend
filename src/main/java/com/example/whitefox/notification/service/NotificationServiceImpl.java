package com.example.whitefox.notification.service;

import com.example.whitefox.notification.dto.CreateNotificationRequest;
import com.example.whitefox.notification.dto.NotificationResponse;
import com.example.whitefox.notification.entity.Notification;
import com.example.whitefox.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public NotificationResponse createNotification(CreateNotificationRequest request) {
        Notification notification = Notification.builder()
                .title(request.getTitle())
                .message(request.getMessage())
                .senderType(request.getSenderType())
                .senderId(request.getSenderId())
                .receiverType(request.getReceiverType())
                .receiverId(request.getReceiverId())
                .notificationType(request.getNotificationType())
                .build();

        return map(repository.save(notification));
    }

    @Override
    public List<NotificationResponse> getAllNotifications() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<NotificationResponse> getByReceiver(String receiverType, UUID receiverId) {
        return repository.findByReceiverTypeAndReceiverId(receiverType, receiverId)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public NotificationResponse markAsRead(UUID notificationId) {
        Notification notification = repository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setIsRead(true);

        return map(repository.save(notification));
    }

    private NotificationResponse map(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .senderType(notification.getSenderType())
                .senderId(notification.getSenderId())
                .receiverType(notification.getReceiverType())
                .receiverId(notification.getReceiverId())
                .notificationType(notification.getNotificationType())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}