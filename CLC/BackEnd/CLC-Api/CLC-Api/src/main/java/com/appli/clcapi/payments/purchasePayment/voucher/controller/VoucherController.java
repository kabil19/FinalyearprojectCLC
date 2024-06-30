package com.appli.clcapi.payments.purchasePayment.voucher.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.purchasePayment.voucher.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/voucher/")
@CrossOrigin(origins = "http://localhost:4200")
public class VoucherController {

private final VoucherService voucherService;
    @GetMapping("getAllVoucher/{purchaseId}")
    public NonPaginatedResponse getAllVoucherOfThePurchaseId(@PathVariable long purchaseId){
        return voucherService.getAllVoucherOfThePurchaseId(purchaseId);
    }
}
