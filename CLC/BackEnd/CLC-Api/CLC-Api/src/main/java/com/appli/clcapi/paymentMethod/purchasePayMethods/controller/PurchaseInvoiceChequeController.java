package com.appli.clcapi.paymentMethod.purchasePayMethods.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.paymentMethod.purchasePayMethods.service.PurchaseInvoiceChequeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/purchaseInvoiceChequePayments")
@CrossOrigin("http://localhost:4200")
public class PurchaseInvoiceChequeController {

private final PurchaseInvoiceChequeService purchaseInvoiceChequeService;
    @GetMapping("/getAllConfirmedPurchaseInvoiceDueCheques")
    public NonPaginatedResponse getAllConfirmedPurchaseInvoiceDueCheques()  {
        return purchaseInvoiceChequeService.getAllConfirmedPurchaseInvoiceDueCheques();
    }
}
