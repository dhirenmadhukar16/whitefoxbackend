package com.example.whitefox.audit.repository;

import com.example.whitefox.audit.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    List<AuditLog> findByModule(String module);

    List<AuditLog> findByReferenceId(UUID referenceId);
}
