package com.example.whitefox.admin.repository;

import com.example.whitefox.admin.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("adminPanelAuditLogRepository")
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
}
