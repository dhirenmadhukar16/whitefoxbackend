package com.example.whitefox.admin.service;

import com.example.whitefox.admin.entity.AuditLog;
import com.example.whitefox.admin.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogService {
    private final AuditLogRepository auditLogRepository;

    public void logAction(String actorName, String actorType, String action, String entityType, String entityId, String ipAddress) {
        AuditLog log = AuditLog.builder()
                .actorName(actorName)
                .actorType(actorType)
                .action(action)
                .entityType(entityType)
                .entityId(entityId)
                .ipAddress(ipAddress)
                .build();
        auditLogRepository.save(log);
    }
}
