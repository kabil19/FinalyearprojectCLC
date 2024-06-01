package com.appli.clcapi.purchase.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.productCart.dto.ProductCartDto;
import com.appli.clcapi.productCart.service.ProductCartService;
import com.appli.clcapi.purchase.dto.TempPurchaseDto;
import com.appli.clcapi.purchase.service.TempPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tempPurchase/")
@CrossOrigin(origins = "http://localhost:4200")
public class TempPurchaseController {

    private final TempPurchaseService tempPurchaseService;
    @PostMapping("createTempPurchase")
    public NonPaginatedResponse createTempPurchase(@RequestBody TempPurchaseDto tempPurchaseDto)
    {
        return tempPurchaseService.addToTempPurchase(tempPurchaseDto);
    }

    @DeleteMapping("deleteTempPurchase/{purchaseId}")
    public NonPaginatedResponse delete(@PathVariable Long purchaseId){
        return tempPurchaseService.deleteTempPurchase(purchaseId);
    }

}
