package com.appli.clcapi.payments.invoicePayments.confirmPayments.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmSalesPaymentsDto;

public interface ConfirmPaymentsService {

    NonPaginatedResponse makePaymentToConfirmInvoice(ConfirmSalesPaymentsDto confirmSalesPaymentsDto);
    NonPaginatedResponse getAllConfirmPaymentsOfConfirmInvoice(Long confirmSalesInvoiceId);

   /* NonPaginatedResponse deletePayment(Long payId);

    NonPaginatedResponse updatePayment(ConfirmSalesPaymentsDto confirmPaymentsDto);

    NonPaginatedResponse getAllPayments(Long invoiceId);

    NonPaginatedResponse selectA_Payment();*/


}
