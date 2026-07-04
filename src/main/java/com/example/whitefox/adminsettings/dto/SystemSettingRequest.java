package com.example.whitefox.adminsettings.dto;

import lombok.Data;

@Data
public class SystemSettingRequest {

    private String settingKey;

    private String settingValue;

    private String description;
}