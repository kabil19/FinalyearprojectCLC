package com.appli.clcapi.paymentMethod.invoicePayMethods.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

public interface ChequePaymentsService {

    NonPaginatedResponse getAllConfirmedSalesInvoiceDueCheques();
    NonPaginatedResponse getAllTempSalesInvoiceDueCheques();
}
