package com.example.whitefox.adminpanel.dto;

import com.example.whitefox.customerbooking.enums.CustomerBookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class AdminBookingResponse {

    private UUID bookingId;

    private UUID customerId;
    private String customerName;
    private String customerPhone;

    private UUID storeId;
    private String storeName;

    private UUID riderId;
    private String riderName;

    private String pickupAddress;

    private LocalDate pickupDate;

    private String pickupTimeSlot;

    private Double estimatedAmount;

    private CustomerBookingStatus status;
}