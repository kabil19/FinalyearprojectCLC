package com.appli.clcapi.confirmInvoice.confirmCartItems.service;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;

public interface ConfirmProductCartService {
    Boolean confirmTheCartItems(Long productCartId, ConfirmInvoiceEntity confirmInvoice);
}
