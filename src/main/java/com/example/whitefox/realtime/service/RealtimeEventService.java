package com.example.whitefox.realtime.service;

import com.example.whitefox.realtime.dto.RealtimeEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RealtimeEventService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendToCustomer(UUID customerId, RealtimeEvent event) {
        event.setCreatedAt(LocalDateTime.now());
        messagingTemplate.convertAndSend(
                "/topic/customer/" + customerId,
                event
        );
    }

    public void sendToStore(UUID storeId, RealtimeEvent event) {
        event.setCreatedAt(LocalDateTime.now());
        messagingTemplate.convertAndSend(
                "/topic/store/" + storeId,
                event
        );
    }

    public void sendToRider(UUID riderId, RealtimeEvent event) {
        event.setCreatedAt(LocalDateTime.now());
        messagingTemplate.convertAndSend(
                "/topic/rider/" + riderId,
                event
        );
    }

    public void sendToAdmin(RealtimeEvent event) {
        event.setCreatedAt(LocalDateTime.now());
        messagingTemplate.convertAndSend(
                "/topic/admin",
                event
        );
    }
}