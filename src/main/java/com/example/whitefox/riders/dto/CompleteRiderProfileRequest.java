package com.example.whitefox.riders.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CompleteRiderProfileRequest {
    private String name;
    private UUID storeId;
}
