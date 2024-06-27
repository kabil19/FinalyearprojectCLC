package com.appli.clcapi.report.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

import java.time.LocalDateTime;

public interface ReportService {
    NonPaginatedResponse selectSalesReportWithInRange(LocalDateTime start,LocalDateTime end);
    NonPaginatedResponse selectPurchaseReportWithInRange(LocalDateTime start,LocalDateTime end);
}
