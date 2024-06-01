package com.appli.clcapi.payments.invoicePayments.confirmPayments.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmPaymentsDto;

public interface ConfirmPaymentsService {

    NonPaginatedResponse addPayment(ConfirmPaymentsDto confirmPaymentsDto);

    NonPaginatedResponse deletePayment(Long payId);

    NonPaginatedResponse updatePayment(ConfirmPaymentsDto confirmPaymentsDto);

    NonPaginatedResponse getAllPayments(Long invoiceId);

    NonPaginatedResponse selectA_Payment();


}
