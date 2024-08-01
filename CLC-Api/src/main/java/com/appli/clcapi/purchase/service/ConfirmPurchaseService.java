package com.appli.clcapi.purchase.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

public interface ConfirmPurchaseService {


    NonPaginatedResponse addToConfirmPurchase(Long purchaseId);
    NonPaginatedResponse getAllConfirmPurchaseInvoices();
    NonPaginatedResponse cancelPurchaseInvoice(Long purchaseId);
    NonPaginatedResponse searchConfirmPurchaseInvoices(String searchCharacter);
}
