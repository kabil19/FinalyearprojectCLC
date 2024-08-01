package com.appli.clcapi.payments.invoicePayments.receipt.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.receipt.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/receipt/")
@CrossOrigin(origins = "http://localhost:4200")
public class ReceiptController {

private final ReceiptService receiptService;
    @GetMapping("getAllReceipts/{invoiceId}")
    public NonPaginatedResponse getAllReceiptsOfTheInvoiceId(@PathVariable long invoiceId){
        return receiptService.getAllReceiptsOfTheInvoiceId(invoiceId);
    }
}
