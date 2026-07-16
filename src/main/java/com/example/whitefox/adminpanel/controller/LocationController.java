package com.example.whitefox.adminpanel.controller;

import com.example.whitefox.adminpanel.dto.LocationUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * App (Rider or Truck) sends location to /app/location/update
     */
    @MessageMapping("/location/update")
    public void receiveLocationUpdate(@Payload LocationUpdate update) {
        if (update.getTimestamp() == null) {
            update.setTimestamp(Instant.now());
        }
        
        // Broadcast to specific topic /topic/location/{entityId}
        String topic = "/topic/location/" + update.getEntityId();
        log.debug("Broadcasting location to {}: {}, {}", topic, update.getLatitude(), update.getLongitude());
        messagingTemplate.convertAndSend(topic, update);
    }
}
