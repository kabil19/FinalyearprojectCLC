package com.appli.clcapi.payments.serviceImple;

import com.appli.clcapi.common.constants.PaymentsConstants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.dto.PaymentsDto;
import com.appli.clcapi.payments.entity.PaymentsEntity;
import com.appli.clcapi.payments.repository.PaymentsRepo;
import com.appli.clcapi.payments.service.PaymentsService;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentsImple implements PaymentsService {

    private final PaymentsRepo paymentsRepo;
    private final TempInvoiceRepo tempInvoiceRepo;
    @Override
    @Transactional
    public NonPaginatedResponse addPayment(PaymentsDto paymentsDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            PaymentsEntity aPayment = PaymentsEntity.builder()
                    .paymentId(paymentsDto.getPaymentId())
                    .paymentType(paymentsDto.getPaymentType())
                    .paidAmount(paymentsDto.getPaidAmount())
                    .paidDate(paymentsDto.getPaidDate())
                    .sellInvoice(new TempInvoiceEntity(paymentsDto.getSellInvoice()))
                    .build();
            paymentsRepo.save(aPayment);
            Optional<TempInvoiceEntity> selectedSellInvoice = tempInvoiceRepo.findById(paymentsDto.getSellInvoice().getTempInvoiceId());
            selectedSellInvoice.get().setPaidAmount(selectedSellInvoice.get().getPaidAmount() + paymentsDto.getPaidAmount());
            tempInvoiceRepo.save(selectedSellInvoice.get());
            response.setResult(null);
            response.setSuccessMessage(PaymentsConstants.PAYMENT_HAS_BEEN_ADDED);
            response.setStatus(HttpStatus.CREATED);

        } catch (Exception e) {
            response.setSuccessMessage(null);
            response.setErrors(Arrays.asList("An error occurred"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public NonPaginatedResponse deletePayment(Long payId) {
        return null;
    }

    @Override
    public NonPaginatedResponse updatePayment(PaymentsDto paymentsDto) {
        return null;
    }

    @Override
    public NonPaginatedResponse getAllPayments(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            List<PaymentsEntity> paymentsEntities = paymentsRepo.findBySellInvoice_TempInvoiceId(invoiceId);
            List<PaymentsDto> paymentsDtos = new ArrayList<>();
            for (PaymentsEntity aPay :paymentsEntities){
                PaymentsDto aPaymentDto = new PaymentsDto(aPay);
                paymentsDtos.add(aPaymentDto);
            }
            response.setResult(paymentsDtos);
            response.setStatus(HttpStatus.OK);
            response.setSuccessMessage("Data is retrieved");
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(Arrays.asList("Couldn't find anything"));
        }
        return response;
    }

    @Override
    public NonPaginatedResponse selectA_Payment() {
        return null;
    }
}
