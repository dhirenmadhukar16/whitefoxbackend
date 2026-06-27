package com.example.whitefox.ordertimeline.dto;

import com.example.whitefox.orders.enums.OrderStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class TimelineResponse {

    private UUID id;

    private OrderStatus status;

    private String action;

    private String remarks;

    private UUID userId;

    private String userName;

    private String module;

    private LocalDateTime actionTime;

}