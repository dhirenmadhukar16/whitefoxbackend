package com.example.whitefox.audit.controller;

import com.example.whitefox.audit.dto.AuditLogResponse;
import com.example.whitefox.audit.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/audit")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    public List<AuditLogResponse> getAllLogs() {
        return auditLogService.getAllLogs();
    }

    @GetMapping("/module/{module}")
    public List<AuditLogResponse> getModuleLogs(
            @PathVariable String module
    ) {
        return auditLogService.getModuleLogs(module);
    }

    @GetMapping("/reference/{referenceId}")
    public List<AuditLogResponse> getReferenceLogs(
            @PathVariable UUID referenceId
    ) {
        return auditLogService.getReferenceLogs(referenceId);
    }
}