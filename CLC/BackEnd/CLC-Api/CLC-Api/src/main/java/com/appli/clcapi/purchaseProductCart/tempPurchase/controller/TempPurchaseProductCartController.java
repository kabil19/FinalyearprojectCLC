package com.appli.clcapi.purchaseProductCart.tempPurchase.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.productCart.dto.ProductCartDto;
import com.appli.clcapi.productCart.service.ProductCartService;
import com.appli.clcapi.purchaseProductCart.tempPurchase.dto.TempPurchaseProductCartDto;
import com.appli.clcapi.purchaseProductCart.tempPurchase.service.TempPurchaseProductCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tempPurchase/")
@CrossOrigin(origins = "http://localhost:4200")
public class TempPurchaseProductCartController {

    private final TempPurchaseProductCartService tempPurchaseProductCartService;
    @PostMapping("register")
    public NonPaginatedResponse addToTempPurchaseCart(@RequestBody TempPurchaseProductCartDto tempPurchaseProductCartDto)
    {
        return tempPurchaseProductCartService.addToTempPurchaseCart(tempPurchaseProductCartDto);
    }

    @DeleteMapping("delete/{proCartId}")
    public NonPaginatedResponse deleteTempPurchaseCartRecord(@PathVariable Long proCartId){
        return tempPurchaseProductCartService.deleteTempPurchaseCartRecord(proCartId);
    }

    @PutMapping("update")
    public NonPaginatedResponse updateTempPurchaseCartRecord(@RequestBody TempPurchaseProductCartDto tempPurchaseProductCartDto){
        return tempPurchaseProductCartService.updateTempPurchaseCartRecord(tempPurchaseProductCartDto);
    }

    @GetMapping("getAll")
    public NonPaginatedResponse getAllTempPurchaseCartItems(){
        return tempPurchaseProductCartService.getAllTempPurchaseCartItems();
    }



    @GetMapping("select/{existingChar}")
    public NonPaginatedResponse selectTempPurchaseRecords( @PathVariable String existingChar){
        return tempPurchaseProductCartService.selectTempPurchaseRecords( existingChar);
    }


}
