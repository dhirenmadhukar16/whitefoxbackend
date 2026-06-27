package com.example.whitefox.audit.service;

import com.example.whitefox.audit.dto.AuditLogResponse;
import com.example.whitefox.audit.entity.AuditLog;
import com.example.whitefox.audit.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;

    @Override
    public void saveLog(
            String actorType,
            UUID actorId,
            String action,
            String module,
            UUID referenceId,
            String description
    ) {

        AuditLog log = AuditLog.builder()
                .actorType(actorType)
                .actorId(actorId)
                .action(action)
                .module(module)
                .referenceId(referenceId)
                .description(description)
                .build();

        repository.save(log);
    }

    @Override
    public List<AuditLogResponse> getAllLogs() {

        return repository.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<AuditLogResponse> getModuleLogs(String module) {

        return repository.findByModule(module)
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public List<AuditLogResponse> getReferenceLogs(UUID referenceId) {

        return repository.findByReferenceId(referenceId)
                .stream()
                .map(this::map)
                .toList();
    }

    private AuditLogResponse map(AuditLog log) {

        return AuditLogResponse.builder()
                .id(log.getId())
                .actorType(log.getActorType())
                .actorId(log.getActorId())
                .action(log.getAction())
                .module(log.getModule())
                .referenceId(log.getReferenceId())
                .description(log.getDescription())
                .createdAt(log.getCreatedAt())
                .build();
    }
}