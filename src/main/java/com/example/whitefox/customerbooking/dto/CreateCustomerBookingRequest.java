package com.example.whitefox.customerbooking.dto;



import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class CreateCustomerBookingRequest {

    private UUID customerId;

    private UUID storeId;

    private String pickupAddress;

    private LocalDate pickupDate;

    private String pickupTimeSlot;

    private String specialInstructions;

    private String deliveryType;

    private String paymentMode;

    private Double amountPaid;

    private List<CreateCustomerBookingItemRequest> items;
}