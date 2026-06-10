package com.example.whitefox.orders.dto;



import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {

    private String serviceType;

    private String itemName;

    private Integer quantity;

    private Double unitPrice;

    private Double totalPrice;
}
