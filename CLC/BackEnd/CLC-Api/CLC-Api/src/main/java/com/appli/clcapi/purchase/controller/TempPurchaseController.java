package com.appli.clcapi.purchase.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
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
    public NonPaginatedResponse deleteTempPurchase(@PathVariable Long purchaseId){
        return tempPurchaseService.deleteTempPurchase(purchaseId);
    }
    @GetMapping("getAllTempPurchase")
    public NonPaginatedResponse getAllTempPurchase(){
        return tempPurchaseService.getAllTempPurchase();
    }
}
