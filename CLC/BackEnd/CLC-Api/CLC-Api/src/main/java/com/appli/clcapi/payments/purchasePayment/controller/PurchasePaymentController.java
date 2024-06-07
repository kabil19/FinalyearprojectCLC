package com.appli.clcapi.payments.purchasePayment.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.tempPayments.dto.TempPaymentsDto;
import com.appli.clcapi.payments.purchasePayment.dto.PurchasePaymentDto;
import com.appli.clcapi.payments.purchasePayment.service.PurchasePaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/purchasePayments/")
@CrossOrigin(origins = "http://localhost:4200")
public class PurchasePaymentController {

    private final PurchasePaymentService purchasePaymentService;
    @PostMapping("addToPurchaseInvoicePayment")
    public NonPaginatedResponse addToPurchaseInvoicePayment(@RequestBody PurchasePaymentDto purchasePaymentDto)
    {
        return purchasePaymentService.addToPurchaseInvoicePayment(purchasePaymentDto);
    }

    @DeleteMapping("deletePurchaseInvoicePayment/{paymentId}")
    public NonPaginatedResponse deletePurchaseInvoicePayment(@PathVariable Long paymentId){
        return purchasePaymentService.deletePurchaseInvoicePayment(paymentId);
    }

    @PutMapping("updatePurchaseInvoicePayment")
    public NonPaginatedResponse updatePurchaseInvoicePayment(@RequestBody PurchasePaymentDto purchasePaymentDto){
        return purchasePaymentService.updatePurchaseInvoicePayment(purchasePaymentDto);
    }

    @GetMapping("getAllPurchaseInvoicePayments/{purchaseId}")
    public NonPaginatedResponse getAllPurchaseInvoicePayments(@PathVariable Long purchaseId){
        return purchasePaymentService.getAllPurchaseInvoicePayments(purchaseId);
    }

//    @GetMapping("selectPurchaseInvoicePayment/{invoiceId}/{existingChar}")
//    public NonPaginatedResponse selectPurchaseInvoicePayment(@PathVariable Long paymentId, @PathVariable String existingChar){
//        return purchasePaymentService.selectPurchaseInvoicePayment();
//    }


}
