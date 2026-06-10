package com.example.whitefox.orders.dto;



import lombok.Data;

import java.util.UUID;

@Data
public class OrderItemRequest {

    private UUID catalogId;

    private Integer quantity;
}