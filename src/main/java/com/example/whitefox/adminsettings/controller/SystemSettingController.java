package com.example.whitefox.adminsettings.controller;

import com.example.whitefox.adminsettings.dto.SystemSettingRequest;
import com.example.whitefox.adminsettings.dto.SystemSettingResponse;
import com.example.whitefox.adminsettings.service.SystemSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-settings")
@RequiredArgsConstructor
public class SystemSettingController {

    private final SystemSettingService settingService;

    @PostMapping
    public SystemSettingResponse createOrUpdate(
            @RequestBody SystemSettingRequest request
    ) {
        return settingService.createOrUpdate(request);
    }

    @GetMapping
    public List<SystemSettingResponse> getAllSettings() {
        return settingService.getAllSettings();
    }

    @GetMapping("/{settingKey}")
    public SystemSettingResponse getByKey(
            @PathVariable String settingKey
    ) {
        return settingService.getByKey(settingKey);
    }
}