package com.appli.clcapi.payments.tempPayments.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.tempPayments.dto.TempPaymentsDto;

public interface TempPaymentsService {

    NonPaginatedResponse addPayment(TempPaymentsDto paymentsDto);

    NonPaginatedResponse deletePayment(Long payId);

    NonPaginatedResponse updatePayment(TempPaymentsDto paymentsDto);

    NonPaginatedResponse getAllPayments(Long invoiceId);

    NonPaginatedResponse selectA_Payment();


}
