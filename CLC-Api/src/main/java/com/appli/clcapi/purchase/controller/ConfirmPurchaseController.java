package com.appli.clcapi.purchase.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.purchase.service.ConfirmPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/confirmPurchase/")
@CrossOrigin(origins = "http://localhost:4200")
public class ConfirmPurchaseController {
    private final ConfirmPurchaseService confirmPurchaseService;


    @PostMapping("addToConfirmPurchase")
    public NonPaginatedResponse addToConfirmPurchase(@RequestBody Long purchaseId)
    {
        return confirmPurchaseService.addToConfirmPurchase(purchaseId);
    }
    @GetMapping("getAllConfirmPurchaseInvoices")
    public NonPaginatedResponse getAllConfirmPurchaseInvoices()
    {
        return confirmPurchaseService.getAllConfirmPurchaseInvoices();
    }

    @GetMapping("searchConfirmInvoice/{searchCharacter}")
    public NonPaginatedResponse searchConfirmInvoice(@PathVariable String searchCharacter)
    {
        return confirmPurchaseService.searchConfirmPurchaseInvoices(searchCharacter);
    }
    @DeleteMapping("cancelPurchase/{purchaseId}")
    public NonPaginatedResponse cancelPurchase(@PathVariable Long purchaseId)
    {
        return confirmPurchaseService.cancelPurchaseInvoice(purchaseId);
    }
}
