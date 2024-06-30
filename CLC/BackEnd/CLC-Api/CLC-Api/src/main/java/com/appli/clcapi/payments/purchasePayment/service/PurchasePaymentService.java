package com.appli.clcapi.payments.purchasePayment.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.tempPayments.dto.TempPaymentsDto;
import com.appli.clcapi.payments.purchasePayment.dto.PurchasePaymentDto;

public interface PurchasePaymentService {

    public NonPaginatedResponse addToPurchaseInvoicePayment(PurchasePaymentDto purchasePaymentDto);

    public NonPaginatedResponse deletePurchaseInvoicePayment(Long paymentId);

    public NonPaginatedResponse updatePurchaseInvoicePayment(PurchasePaymentDto purchasePaymentDto);

    NonPaginatedResponse getAllPurchaseInvoicePayments(Long purchaseInvoiceId);
}
