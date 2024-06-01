package com.appli.clcapi.purchase.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.purchase.dto.TempPurchaseDto;

public interface TempPurchaseService {
    NonPaginatedResponse addToTempPurchase(TempPurchaseDto tempPurchaseDto);

    NonPaginatedResponse deleteTempPurchase(Long purchaseId);
}
