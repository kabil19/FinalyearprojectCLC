package com.appli.clcapi.purchase.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

public interface ConfirmPurchaseService {


    NonPaginatedResponse addToConfirmThePurchase(Long purchaseId);
    NonPaginatedResponse getAllConfirmPurchaseInvoices();
}
