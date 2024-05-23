package com.appli.clcapi.payments.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.dto.PaymentsDto;
import com.appli.clcapi.payments.service.PaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/payments/")
@CrossOrigin(origins = "http://localhost:4200")
public class PaymentsController {


    private final PaymentsService paymentsService;
    @PostMapping("addPayment")
    public NonPaginatedResponse addPayment(@RequestBody PaymentsDto paymentDto)
    {
        return paymentsService.addPayment(paymentDto);
    }

    @DeleteMapping("deletePayment/{paymentId}")
    public NonPaginatedResponse deletePayment(@PathVariable Long paymentId){
        return paymentsService.deletePayment(paymentId);
    }

    @PutMapping("updatePayment")
    public NonPaginatedResponse updatePayment(@RequestBody PaymentsDto paymentsDto){
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
