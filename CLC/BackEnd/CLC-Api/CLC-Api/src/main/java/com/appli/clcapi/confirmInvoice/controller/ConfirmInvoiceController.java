package com.appli.clcapi.confirmInvoice.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.service.ConfirmInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/confirmInvoice/")
@CrossOrigin(origins = "*")
public class ConfirmInvoiceController {



        private final ConfirmInvoiceService confirmInvoiceService;
        @PostMapping("addToConfirmInvoice")
        public NonPaginatedResponse addToConfirmInvoice(@RequestBody  Long invoiceId)throws Exception{
            return confirmInvoiceService.insertIntoConfirmInvoice(invoiceId);
        }


}
