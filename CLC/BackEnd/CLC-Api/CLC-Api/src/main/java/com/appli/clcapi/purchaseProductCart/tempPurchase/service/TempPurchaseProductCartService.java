package com.appli.clcapi.purchaseProductCart.tempPurchase.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.purchaseProductCart.tempPurchase.dto.TempPurchaseProductCartDto;

public interface TempPurchaseProductCartService {

    NonPaginatedResponse addToTempPurchaseCart(TempPurchaseProductCartDto tempPurchaseProductCartDto) ;
    NonPaginatedResponse deleteTempPurchaseCartRecord(Long proCartId) ;
    NonPaginatedResponse getAllTempPurchaseCartItems(Long purchaseId);
    NonPaginatedResponse selectTempPurchaseCartRecords(Long purchaseId, String exitingChar);
    NonPaginatedResponse updateTempPurchaseCartRecord(TempPurchaseProductCartDto tempPurchaseProductCartDto);

}
