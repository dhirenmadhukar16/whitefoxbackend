package com.example.whitefox.customerbooking.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RescheduleBookingRequest {

    private LocalDate pickupDate;

    private String pickupTimeSlot;
}