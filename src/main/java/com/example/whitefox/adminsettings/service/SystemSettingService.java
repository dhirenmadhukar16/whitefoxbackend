package com.example.whitefox.adminsettings.service;

import com.example.whitefox.adminsettings.dto.SystemSettingRequest;
import com.example.whitefox.adminsettings.dto.SystemSettingResponse;

import java.util.List;

public interface SystemSettingService {

    SystemSettingResponse createOrUpdate(SystemSettingRequest request);

    SystemSettingResponse getByKey(String settingKey);

    List<SystemSettingResponse> getAllSettings();
}