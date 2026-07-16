package com.example.whitefox.customerbooking.dto;



import com.example.whitefox.customerbooking.enums.CustomerBookingStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CustomerBookingResponse {

    private UUID id;

    private UUID customerId;

    private String customerName;

    private String customerPhone;

    private UUID storeId;

    private String storeName;

    private UUID assignedRiderId;

    private String assignedRiderName;

    private String pickupAddress;

    private LocalDate pickupDate;

    private String pickupTimeSlot;

    private String specialInstructions;

    private Double estimatedAmount;

    private CustomerBookingStatus status;

    private String rejectionReason;

    private String deliveryType;

    private String paymentMode;

    private Double amountPaid;

    private List<CustomerBookingItemResponse> items;
}
