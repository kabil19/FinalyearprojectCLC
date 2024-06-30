package com.appli.clcapi.payments.invoicePayments.receipt.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

public interface ReceiptService {

    NonPaginatedResponse getAllReceiptsOfTheInvoiceId(long invoiceId);
}
