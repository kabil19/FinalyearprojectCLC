package com.appli.clcapi.payments.purchasePayment.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

import com.appli.clcapi.payments.purchasePayment.dto.PurchasePaymentDto;

public interface PurchasePaymentService {

     NonPaginatedResponse addToPurchaseInvoicePayment(PurchasePaymentDto purchasePaymentDto);

     NonPaginatedResponse deletePurchaseInvoicePayment(Long paymentId);

     NonPaginatedResponse updatePurchaseInvoicePayment(PurchasePaymentDto purchasePaymentDto);

    NonPaginatedResponse getAllPurchaseInvoicePayments(Long purchaseInvoiceId);
}
