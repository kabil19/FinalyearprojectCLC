package com.appli.clcapi.paymentMethod.purchasePayMethods.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

public interface PurchaseInvoiceChequeService {

    NonPaginatedResponse getAllConfirmedPurchaseInvoiceDueCheques();
}
