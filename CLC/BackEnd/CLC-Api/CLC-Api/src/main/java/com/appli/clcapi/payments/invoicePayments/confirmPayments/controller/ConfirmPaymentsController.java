package com.appli.clcapi.payments.invoicePayments.confirmPayments.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmPaymentsDto;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.service.ConfirmPaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/ConfirmPayments/")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfirmPaymentsController {


    private final ConfirmPaymentsService confirmPaymentsService;
    @PostMapping("addPayment")
    public NonPaginatedResponse addPayment(@RequestBody ConfirmPaymentsDto paymentDto)
    {
        return confirmPaymentsService.addPayment(paymentDto);
    }

    @DeleteMapping("deletePayment/{paymentId}")
    public NonPaginatedResponse deletePayment(@PathVariable Long paymentId){
        return confirmPaymentsService.deletePayment(paymentId);
    }

    @PutMapping("updatePayment")
    public NonPaginatedResponse updatePayment(@RequestBody ConfirmPaymentsDto confirmPaymentsDto){
        return confirmPaymentsService.updatePayment(confirmPaymentsDto);
    }

    @GetMapping("getAllPayments/{invoiceId}")
    public NonPaginatedResponse getAllPayments(@PathVariable Long invoiceId){
        return confirmPaymentsService.getAllPayments(invoiceId);
    }

    @GetMapping("select/{invoiceId}/{existingChar}")
    public NonPaginatedResponse select(@PathVariable Long paymentId, @PathVariable String existingChar){
        return confirmPaymentsService.selectA_Payment();
    }



}
