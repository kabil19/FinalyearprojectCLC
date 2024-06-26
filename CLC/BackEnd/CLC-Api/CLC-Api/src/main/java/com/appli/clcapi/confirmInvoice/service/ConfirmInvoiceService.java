package com.appli.clcapi.confirmInvoice.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

public interface ConfirmInvoiceService {

    NonPaginatedResponse insertIntoConfirmInvoice(Long invoiceId);
    NonPaginatedResponse getAllConfirmedInvoices();
    NonPaginatedResponse getConfirmedInvoiceByInvoiceNumber(long invoiceNum);
    NonPaginatedResponse searchConfirmedSalesInvoice(String character);

}
