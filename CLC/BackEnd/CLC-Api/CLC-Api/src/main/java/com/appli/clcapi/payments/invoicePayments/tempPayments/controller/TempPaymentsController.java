package com.appli.clcapi.payments.invoicePayments.tempPayments.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.tempPayments.dto.TempPaymentsDto;
import com.appli.clcapi.payments.invoicePayments.tempPayments.service.TempPaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/payments/")
@CrossOrigin(origins = "http://localhost:4200")
public class TempPaymentsController {


    private final TempPaymentsService paymentsService;
    @PostMapping("addPayment")
    public NonPaginatedResponse addPayment(@RequestBody TempPaymentsDto paymentDto)
    {
        return paymentsService.addPayment(paymentDto);
    }

    @DeleteMapping("deletePayment/{paymentId}")
    public NonPaginatedResponse deletePayment(@PathVariable Long paymentId){
        return paymentsService.deletePayment(paymentId);
    }

    @PutMapping("updatePayment")
    public NonPaginatedResponse updatePayment(@RequestBody TempPaymentsDto paymentsDto){
        return paymentsService.updatePayment(paymentsDto);
    }

    @GetMapping("getAllPayments/{invoiceId}")
    public NonPaginatedResponse getAllPayments(@PathVariable Long invoiceId){
        return paymentsService.getAllPayments(invoiceId);
    }

    @GetMapping("select/{invoiceId}/{existingChar}")
    public NonPaginatedResponse select(@PathVariable Long paymentId, @PathVariable String existingChar){
        return paymentsService.selectA_Payment();
    }



}
