package com.example.whitefox.tracking.dto;



import com.example.whitefox.tracking.enums.GarmentStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TrackingHistoryResponse {

    private GarmentStatus status;

    private String remarks;

    private LocalDateTime createdAt;
}
