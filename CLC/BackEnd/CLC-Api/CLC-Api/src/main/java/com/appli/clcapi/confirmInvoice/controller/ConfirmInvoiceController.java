package com.appli.clcapi.confirmInvoice.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.confirmCartItems.service.ConfirmProductCartService;
import com.appli.clcapi.confirmInvoice.service.ConfirmInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/confirmInvoice/")
@CrossOrigin("http://localhost:4200")
public class ConfirmInvoiceController {


    private final ConfirmInvoiceService confirmInvoiceService;
    private final ConfirmProductCartService confirmProductCartService;

    @PostMapping("addToConfirmInvoice")
    public NonPaginatedResponse addToConfirmInvoice(@RequestBody Long invoiceId) {
        return confirmInvoiceService.insertIntoConfirmInvoice(invoiceId);
    }

    @GetMapping("getAllConfirmedInvoices")
    public NonPaginatedResponse getAllConfirmedInvoices()  {
        return confirmInvoiceService.getAllConfirmedInvoices();
    }

    @GetMapping("getConfirmedInvoiceByInvoiceNumber/{invoiceNo}")
    public NonPaginatedResponse getConfirmedInvoiceByInvoiceNumber(@PathVariable long invoiceNo)  {
        return confirmInvoiceService.getConfirmedInvoiceByInvoiceNumber(invoiceNo);
    }

    @GetMapping("searchConfirmedSalesInvoice/{searchCharacter}")
    public NonPaginatedResponse searchConfirmedSalesInvoice(@PathVariable String searchCharacter) {
        return confirmInvoiceService.searchConfirmedSalesInvoice(searchCharacter);
    }
    @GetMapping("getAllConfirmedProCartItemsByInvoiceId/{invoiceId}")
    public NonPaginatedResponse getAllConfirmedProCartItemsByInvoiceId(@PathVariable long invoiceId) {
        return confirmProductCartService.getAllConfirmedProCartItemsByInvoiceId(invoiceId);
    }


}
