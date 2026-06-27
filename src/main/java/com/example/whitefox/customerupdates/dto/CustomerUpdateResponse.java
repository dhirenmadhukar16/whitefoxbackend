package com.example.whitefox.customerupdates.dto;



import com.example.whitefox.customerupdates.enums.CustomerUpdateType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class CustomerUpdateResponse {

    private UUID id;

    private UUID customerId;

    private UUID orderId;

    private CustomerUpdateType updateType;

    private String title;

    private String description;

    private Boolean readStatus;

    private LocalDateTime createdAt;
}