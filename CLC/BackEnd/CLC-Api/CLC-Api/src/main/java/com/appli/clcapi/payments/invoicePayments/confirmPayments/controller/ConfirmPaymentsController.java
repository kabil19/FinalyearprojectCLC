package com.appli.clcapi.payments.invoicePayments.confirmPayments.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmPaymentsDto;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.service.ConfirmPaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/ConfirmSalesInvoicePayments/")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfirmPaymentsController {


    private final ConfirmPaymentsService confirmPaymentsService;
    @PostMapping("makePaymentToConfirmInvoice")
    public NonPaginatedResponse makePaymentToConfirmInvoice(@RequestBody ConfirmPaymentsDto paymentDto)
    {
        return confirmPaymentsService.makePaymentToConfirmInvoice(paymentDto);
    }

    /*@DeleteMapping("deletePayment/{paymentId}")
    public NonPaginatedResponse deletePayment(@PathVariable Long paymentId){
        return confirmPaymentsService.deletePayment(paymentId);
    }

    @PutMapping("updatePayment")
    public NonPaginatedResponse updatePayment(@RequestBody ConfirmPaymentsDto confirmPaymentsDto){
        return confirmPaymentsService.updatePayment(confirmPaymentsDto);
    }*/

    @GetMapping("getAllConfirmPaymentsOfConfirmInvoice/{confirmSalesInvoiceId}")
    public NonPaginatedResponse getAllConfirmPaymentsOfConfirmInvoices(@PathVariable Long confirmSalesInvoiceId){
        return confirmPaymentsService.getAllConfirmPaymentsOfConfirmInvoice(confirmSalesInvoiceId);
    }

   /* @GetMapping("select/{invoiceId}/{existingChar}")
    public NonPaginatedResponse select(@PathVariable Long paymentId, @PathVariable String existingChar){
        return confirmPaymentsService.selectA_Payment();
    }
*/


}
