package com.example.whitefox.audit.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class AuditLogResponse {

    private UUID id;

    private String actorType;

    private UUID actorId;

    private String action;

    private String module;

    private UUID referenceId;

    private String description;

    private LocalDateTime createdAt;
}