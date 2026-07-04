package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AdminHqOperationsResponse {

    private Long totalAtHq;

    private Long receivedAtHq;

    private Long washing;

    private Long ironing;

    private Long packing;

    private Long readyForDispatch;

    private Long sentBackToStore;
}