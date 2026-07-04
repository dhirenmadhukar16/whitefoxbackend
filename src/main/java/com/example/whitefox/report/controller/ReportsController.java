package com.example.whitefox.report.controller;



import com.example.whitefox.report.dto.ReportsResponse;
import com.example.whitefox.report.dto.StoreReportResponse;
import com.example.whitefox.report.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping
    public ReportsResponse getOverallReport() {
        return reportsService.getOverallReport();
    }

    @GetMapping("/stores/{storeId}")
    public StoreReportResponse getStoreReport(
            @PathVariable UUID storeId
    ) {
        return reportsService.getStoreReport(storeId);
    }
}
