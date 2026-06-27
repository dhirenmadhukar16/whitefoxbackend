package com.example.whitefox.adminsettings.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class SystemSettingResponse {

    private UUID id;

    private String settingKey;

    private String settingValue;

    private String description;

    private LocalDateTime updatedAt;
}