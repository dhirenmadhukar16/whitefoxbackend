package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.SystemReport;
import com.example.whitefox.admin.repository.SystemReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/reports")
@CrossOrigin("*")
public class ReportController {

    @Autowired
    private SystemReportRepository reportRepository;

    @GetMapping
    public ResponseEntity<List<SystemReport>> getReports() {
        return ResponseEntity.ok(reportRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<SystemReport> createReport(@RequestBody SystemReport report) {
        return ResponseEntity.ok(reportRepository.save(report));
    }
}
