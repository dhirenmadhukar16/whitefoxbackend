package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminGarmentStatsResponse {

    private Long totalGarments;

    private Long atStore;
    private Long sentToHq;
    private Long receivedAtHq;

    private Long washing;
    private Long ironing;
    private Long packing;

    private Long ready;
    private Long delivered;
}