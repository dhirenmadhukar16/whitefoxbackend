package com.example.whitefox.adminsettings.service;

import com.example.whitefox.adminsettings.dto.SystemSettingRequest;
import com.example.whitefox.adminsettings.dto.SystemSettingResponse;
import com.example.whitefox.adminsettings.entity.SystemSetting;
import com.example.whitefox.adminsettings.repository.SystemSettingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemSettingServiceImpl implements SystemSettingService {

    private final SystemSettingRepository settingRepository;

    @Override
    public SystemSettingResponse createOrUpdate(SystemSettingRequest request) {

        SystemSetting setting = settingRepository
                .findBySettingKey(request.getSettingKey())
                .orElse(
                        SystemSetting.builder()
                                .settingKey(request.getSettingKey())
                                .build()
                );

        setting.setSettingValue(request.getSettingValue());
        setting.setDescription(request.getDescription());

        return map(settingRepository.save(setting));
    }

    @Override
    public SystemSettingResponse getByKey(String settingKey) {

        SystemSetting setting = settingRepository
                .findBySettingKey(settingKey)
                .orElseThrow(() -> new RuntimeException("Setting not found"));

        return map(setting);
    }

    @Override
    public List<SystemSettingResponse> getAllSettings() {
        return settingRepository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    private SystemSettingResponse map(SystemSetting setting) {
        return SystemSettingResponse.builder()
                .id(setting.getId())
                .settingKey(setting.getSettingKey())
                .settingValue(setting.getSettingValue())
                .description(setting.getDescription())
                .updatedAt(setting.getUpdatedAt())
                .build();
    }
}