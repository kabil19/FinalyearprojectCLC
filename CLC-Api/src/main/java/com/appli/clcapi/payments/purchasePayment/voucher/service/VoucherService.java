package com.appli.clcapi.payments.purchasePayment.voucher.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

public interface VoucherService {

    NonPaginatedResponse getAllVoucherOfThePurchaseId(Long purchaseId);
}
