package com.example.whitefox.admin.repository;

import com.example.whitefox.admin.entity.SystemReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SystemReportRepository extends JpaRepository<SystemReport, UUID> {
}
