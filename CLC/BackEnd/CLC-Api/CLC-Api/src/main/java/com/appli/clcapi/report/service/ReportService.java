package com.appli.clcapi.report.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

import java.util.Date;

public interface ReportService {
    NonPaginatedResponse selectSalesReportWithInRange(Date start, Date end);
}
