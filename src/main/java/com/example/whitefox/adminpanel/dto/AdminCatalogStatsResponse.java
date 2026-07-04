package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminCatalogStatsResponse {

    private Long totalServices;

    private Long activeServices;

    private Long inactiveServices;

    private Long laundryServices;

    private Long dryCleaningServices;

    private Long shoeCleaningServices;

    private Long ironingServices;
}