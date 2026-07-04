package com.example.whitefox.notification.repository;

import com.example.whitefox.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository
        extends JpaRepository<Notification, UUID> {

    List<Notification> findByReceiverId(UUID receiverId);

    List<Notification> findByReceiverType(String receiverType);

    List<Notification> findByReceiverTypeAndReceiverId(
            String receiverType,
            UUID receiverId
    );
}