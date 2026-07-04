package com.example.whitefox.ordertimeline.controller;

import com.example.whitefox.ordertimeline.dto.TimelineResponse;
import com.example.whitefox.ordertimeline.service.OrderTimelineService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderTimelineController {

    private final OrderTimelineService service;

    @GetMapping("/{orderId}/timeline")
    public List<TimelineResponse> getTimeline(
            @PathVariable UUID orderId
    ) {

        return service.getTimeline(orderId);

    }
}