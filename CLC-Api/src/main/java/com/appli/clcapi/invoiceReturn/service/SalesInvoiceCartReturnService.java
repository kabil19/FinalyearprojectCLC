package com.appli.clcapi.invoiceReturn.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.invoiceReturn.dto.SalesInvoiceCartReturnDto;

public interface SalesInvoiceCartReturnService {

    NonPaginatedResponse addIntoSalesReturnCart(SalesInvoiceCartReturnDto salesInvoiceCartReturnDto);
    NonPaginatedResponse retrieveRemainingCartItems(long salesRetInvoiceId);
}
