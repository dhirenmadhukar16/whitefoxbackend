package com.example.whitefox.customerupdates.dto;



import com.example.whitefox.customerupdates.enums.CustomerUpdateType;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateCustomerUpdateRequest {

    private UUID customerId;

    private UUID orderId;

    private CustomerUpdateType updateType;

    private String title;

    private String description;
}
