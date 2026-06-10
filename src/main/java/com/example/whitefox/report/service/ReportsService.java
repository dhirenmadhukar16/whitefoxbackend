package com.example.whitefox.report.service;



import com.example.whitefox.report.dto.ReportsResponse;
import com.example.whitefox.report.dto.StoreReportResponse;

import java.util.UUID;

public interface ReportsService {

    ReportsResponse getOverallReport();

    StoreReportResponse getStoreReport(UUID storeId);
}
