package com.appli.clcapi.purchaseProductCart.tempPurchase.controller;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.purchaseProductCart.tempPurchase.dto.TempPurchaseProductCartDto;
import com.appli.clcapi.purchaseProductCart.tempPurchase.service.TempPurchaseProductCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/tempPurchaseCart/")
@CrossOrigin(origins = "http://localhost:4200")
public class TempPurchaseProductCartController {

    private final TempPurchaseProductCartService tempPurchaseProductCartService;
    @PostMapping("addToTempPurchaseCart")
    public NonPaginatedResponse addToTempPurchaseCart(@RequestBody TempPurchaseProductCartDto tempPurchaseProductCartDto)
    {
        return tempPurchaseProductCartService.addToTempPurchaseCart(tempPurchaseProductCartDto);
    }

    @DeleteMapping("deleteTempPurchaseCartRecord/{proCartId}")
    public NonPaginatedResponse deleteTempPurchaseCartRecord(@PathVariable Long proCartId){
        return tempPurchaseProductCartService.deleteTempPurchaseCartRecord(proCartId);
    }

    @PutMapping("updateTempPurchaseCartRecord")
    public NonPaginatedResponse updateTempPurchaseCartRecord(@RequestBody TempPurchaseProductCartDto tempPurchaseProductCartDto){
        return tempPurchaseProductCartService.updateTempPurchaseCartRecord(tempPurchaseProductCartDto);
    }

    @GetMapping("getAllTempPurchaseCartItems/{purchaseId}")
    public NonPaginatedResponse getAllTempPurchaseCartItems(@PathVariable Long purchaseId){
        return tempPurchaseProductCartService.getAllTempPurchaseCartItems(purchaseId);
    }

    @GetMapping("selectTempPurchaseCartRecords/{purchaseId}/{existingChar}")
    public NonPaginatedResponse selectTempPurchaseCartRecords(@PathVariable Long purchaseId,@PathVariable String existingChar){
        return tempPurchaseProductCartService.selectTempPurchaseCartRecords(purchaseId ,existingChar);
    }
}
