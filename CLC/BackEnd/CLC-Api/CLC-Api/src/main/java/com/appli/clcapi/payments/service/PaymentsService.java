package com.appli.clcapi.payments.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.dto.PaymentsDto;

public interface PaymentsService {

    NonPaginatedResponse addPayment(PaymentsDto paymentsDto);

    NonPaginatedResponse deletePayment(Long payId);

    NonPaginatedResponse updatePayment(PaymentsDto paymentsDto);

    NonPaginatedResponse getAllPayments(Long invoiceId);

    NonPaginatedResponse selectA_Payment();


}
