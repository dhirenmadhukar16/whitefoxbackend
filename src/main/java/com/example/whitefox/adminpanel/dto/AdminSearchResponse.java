package com.example.whitefox.adminpanel.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AdminSearchResponse {

    private String type;

    private UUID id;

    private String title;

    private String subtitle;

    private String status;

    private String extraInfo;
}