package com.example.whitefox.customerbooking.dto;



import lombok.Data;

import java.util.UUID;

@Data
public class CreateCustomerBookingItemRequest {

    private UUID catalogId;

    private Integer quantity;
}
