package com.appli.clcapi.report.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/reports/")
@CrossOrigin("http://localhost:4200")
public class ReportController {

    private final ReportService reportService;
    @GetMapping("selectSalesReportWithInRange")
    public NonPaginatedResponse selectSalesReportWithInRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reportService.selectSalesReportWithInRange(startDate, endDate);

    }
    @GetMapping("selectPurchaseReportWithInRange")
    public NonPaginatedResponse selectPurchaseReportWithInRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return reportService.selectPurchaseReportWithInRange(startDate, endDate);

    }

    @GetMapping("selectAllPurchaseInvoicePaymentsWithInRange")
    public NonPaginatedResponse selectAllPurchaseInvoicePaymentsWithInRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        return reportService.selectAllPurchaseInvoicePaymentsWithInRange(startDate, endDate);
    }
    @GetMapping("selectAllSalesInvoicePaymentsWithInRange")
    public NonPaginatedResponse selectAllSalesInvoicePaymentsWithInRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        return reportService.selectAllSalesInvoicePaymentsWithInRange(startDate, endDate);
    }
    @GetMapping("selectAllPaymentsOfTheSalesInvoiceWithInTheRange/{confirmSalesInvoiceId}")
    public NonPaginatedResponse selectAllPaymentsOfTheSalesInvoiceWithInTheRange(
            @PathVariable Long confirmSalesInvoiceId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        return reportService.selectAllPaymentsOfTheSalesInvoiceWithInTheRange(confirmSalesInvoiceId,startDate, endDate);
    }
    @GetMapping("selectAllPaymentsOfThePurchaseInvoiceWithInTheRange/{purchaseInvoiceId}")
    public NonPaginatedResponse selectAllPaymentsOfThePurchaseInvoiceWithInTheRange(
            @PathVariable Long purchaseInvoiceId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate){
        return reportService.selectAllPaymentsOfThePurchaseInvoiceWithInTheRange(purchaseInvoiceId,startDate, endDate);
    }
}
