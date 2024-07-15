package com.appli.clcapi.paymentMethod.invoicePayMethods.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.paymentMethod.invoicePayMethods.service.ChequePaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/salesInvoiceChequePayments/")
@CrossOrigin("http://localhost:4200")
public class ChequePaymentController {

    private final ChequePaymentsService chequePaymentsService;
    @GetMapping("getAllConfirmedSalesInvoiceDueCheques")
    public NonPaginatedResponse getAllConfirmedSalesInvoiceDueCheques()  {
        return chequePaymentsService.getAllConfirmedSalesInvoiceDueCheques();
    }

    @GetMapping("getAllTempSalesInvoiceDueCheques")
    public NonPaginatedResponse getAllTempSalesInvoiceDueCheques()  {
        return chequePaymentsService.getAllTempSalesInvoiceDueCheques();
    }
}
