package com.example.whitefox.notification.controller;

import com.example.whitefox.notification.dto.CreateNotificationRequest;
import com.example.whitefox.notification.dto.NotificationResponse;
import com.example.whitefox.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public NotificationResponse createNotification(
            @RequestBody CreateNotificationRequest request
    ) {
        return notificationService.createNotification(request);
    }

    @GetMapping
    public List<NotificationResponse> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping("/{receiverType}/{receiverId}")
    public List<NotificationResponse> getByReceiver(
            @PathVariable String receiverType,
            @PathVariable UUID receiverId
    ) {
        return notificationService.getByReceiver(receiverType, receiverId);
    }

    @PatchMapping("/{notificationId}/read")
    public NotificationResponse markAsRead(
            @PathVariable UUID notificationId
    ) {
        return notificationService.markAsRead(notificationId);
    }
}