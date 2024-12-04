package com.appli.clcapi.confirmInvoice.confirmCartItems.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;

public interface ConfirmProductCartService {
    Boolean confirmTheCartItems(Long productCartId, ConfirmSalesInvoiceEntity confirmInvoice);
    NonPaginatedResponse getAllConfirmedProCartItemsByInvoiceId(Long invoiceId);

}
