package com.appli.clcapi.purchaseProductCart.tempPurchase.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.productCart.dto.ProductCartDto;
import com.appli.clcapi.purchaseProductCart.tempPurchase.dto.TempPurchaseProductCartDto;

public interface TempPurchaseProductCartService {

    NonPaginatedResponse addToTempPurchaseCart(TempPurchaseProductCartDto tempPurchaseProductCartDto) ;
    NonPaginatedResponse deleteTempPurchaseCartRecord(Long proCartId) ;
    NonPaginatedResponse getAllTempPurchaseCartItems();
    NonPaginatedResponse selectTempPurchaseRecords(String exitingChar);
    NonPaginatedResponse updateTempPurchaseCartRecord(TempPurchaseProductCartDto tempPurchaseProductCartDto);

}
