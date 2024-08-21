package com.appli.clcapi.report.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

import java.time.LocalDateTime;

public interface ReportService {
    NonPaginatedResponse selectSalesReportWithInRange(LocalDateTime start,LocalDateTime end);
    NonPaginatedResponse selectPurchaseReportWithInRange(LocalDateTime start,LocalDateTime end);

    NonPaginatedResponse selectAllPurchaseInvoicePaymentsWithInRange(LocalDateTime start,LocalDateTime end);
    NonPaginatedResponse selectAllSalesInvoicePaymentsWithInRange(LocalDateTime start,LocalDateTime end);
    NonPaginatedResponse selectAllPaymentsOfTheSalesInvoiceWithInTheRange(Long salesInvoiceId,LocalDateTime start,LocalDateTime end);
    NonPaginatedResponse selectAllPaymentsOfThePurchaseInvoiceWithInTheRange(Long purchaseId,LocalDateTime start,LocalDateTime end);
    NonPaginatedResponse selectAllPaymentsOfaCustomerWithInRange(Long custId,LocalDateTime start,LocalDateTime end);
    NonPaginatedResponse selectAllPaymentsOfaVendorWithInRange(Long vendorId, LocalDateTime startDate, LocalDateTime endDate);

    NonPaginatedResponse getStockInPriceRange(Double startPrice, Double endPrice);
}
