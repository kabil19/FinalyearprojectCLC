package com.appli.clcapi.invoiceReturn.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.invoiceReturn.dto.SalesInvoiceCartReturnDto;
import com.appli.clcapi.invoiceReturn.service.SalesInvoiceCartReturnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/returnInvoiceProduct/")
@CrossOrigin("http://localhost:4200")
public class SalesInvoiceCartReturnController {

    private final SalesInvoiceCartReturnService salesInvoiceCartReturnService;
    @PostMapping("addToReturnCart")
    public NonPaginatedResponse addToConfirmInvoice(@RequestBody SalesInvoiceCartReturnDto salesInvoiceCartReturnDto) {
        return salesInvoiceCartReturnService.addIntoSalesReturnCart(salesInvoiceCartReturnDto);
    }
    @GetMapping("retrieveRemainingCartItems/{salesRetInvoiceId}")
    public NonPaginatedResponse retrieveRemainingCartItems(@PathVariable long salesRetInvoiceId) {
        return salesInvoiceCartReturnService.retrieveRemainingCartItems(salesRetInvoiceId);
    }

}
