package com.appli.clcapi.confirmInvoice.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

public interface ConfirmInvoiceService {

    NonPaginatedResponse insertIntoConfirmInvoice(Long invoiceId);

}
