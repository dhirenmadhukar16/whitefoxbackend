package com.example.whitefox.ordertimeline.service;

import com.example.whitefox.orders.entity.LaundryOrder;
import com.example.whitefox.orders.enums.OrderStatus;
import com.example.whitefox.ordertimeline.dto.TimelineResponse;

import java.util.List;
import java.util.UUID;

public interface OrderTimelineService {

    void addTimeline(
            LaundryOrder order,
            OrderStatus status,
            String action,
            String remarks,
            UUID userId,
            String userName,
            String module
    );

    List<TimelineResponse> getTimeline(UUID orderId);

}