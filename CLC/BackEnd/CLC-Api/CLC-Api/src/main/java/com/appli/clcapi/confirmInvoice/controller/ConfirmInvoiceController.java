package com.appli.clcapi.confirmInvoice.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.service.ConfirmInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/confirmInvoice/")
@CrossOrigin("http://localhost:4200")
public class ConfirmInvoiceController {


        private final ConfirmInvoiceService confirmInvoiceService;
        @PostMapping("addToConfirmInvoice")
        public NonPaginatedResponse addToConfirmInvoice(@RequestBody  Long invoiceId){
            return confirmInvoiceService.insertIntoConfirmInvoice(invoiceId);
        }


    @GetMapping("getAllConfirmedInvoices")
    public NonPaginatedResponse getAllConfirmedInvoices()throws Exception{
        return confirmInvoiceService.getAllConfirmedInvoices();
    }

    @GetMapping("searchConfirmedSalesInvoice/{searchCharacter}")
    public NonPaginatedResponse searchConfirmedSalesInvoice(@PathVariable String searchCharacter)
    {
        return confirmInvoiceService.searchConfirmedSalesInvoice(searchCharacter);
    }


}
