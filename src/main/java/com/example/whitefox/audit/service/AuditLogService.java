package com.example.whitefox.audit.service;

import com.example.whitefox.audit.dto.AuditLogResponse;

import java.util.List;
import java.util.UUID;

public interface AuditLogService {

    void saveLog(
            String actorType,
            UUID actorId,
            String action,
            String module,
            UUID referenceId,
            String description
    );

    List<AuditLogResponse> getAllLogs();

    List<AuditLogResponse> getModuleLogs(String module);

    List<AuditLogResponse> getReferenceLogs(UUID referenceId);
}