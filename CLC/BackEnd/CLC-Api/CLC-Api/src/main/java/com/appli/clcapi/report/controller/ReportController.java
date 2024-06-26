package com.appli.clcapi.report.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/reports/")
@CrossOrigin("http://localhost:4200")
public class ReportController {

    private final ReportService reportService;
    @GetMapping("selectSalesReportWithInRange")
    public NonPaginatedResponse selectSalesReportWithInRange(@RequestParam("startDate") Date startDate, @RequestParam("endDate") Date endDate) {
        return reportService.selectSalesReportWithInRange(startDate, endDate);

    }
}
