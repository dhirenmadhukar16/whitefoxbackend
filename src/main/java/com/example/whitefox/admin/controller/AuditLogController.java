package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.AuditLog;
import com.example.whitefox.admin.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("adminPanelAuditLogController")
@RequestMapping("/api/admin-panel/audit")
@CrossOrigin("*")
public class AuditLogController {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @GetMapping
    public ResponseEntity<List<AuditLog>> getLogs() {
        return ResponseEntity.ok(auditLogRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<AuditLog> createLog(@RequestBody AuditLog log) {
        return ResponseEntity.ok(auditLogRepository.save(log));
    }
}
