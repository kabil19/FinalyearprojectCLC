package com.appli.clcapi.payments.serviceImple;

import com.appli.clcapi.common.constants.PaymentsConstants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.dto.PaymentsDto;
import com.appli.clcapi.payments.entity.PaymentsEntity;
import com.appli.clcapi.payments.paymentMethod.entity.CardEntity;
import com.appli.clcapi.payments.paymentMethod.entity.CashEntity;
import com.appli.clcapi.payments.paymentMethod.entity.ChequeEntity;
import com.appli.clcapi.payments.paymentMethod.repository.CardRepo;
import com.appli.clcapi.payments.paymentMethod.repository.CashRepo;
import com.appli.clcapi.payments.paymentMethod.repository.ChequeRepo;
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
    private final CardRepo cardRepo;
    private final CashRepo cashRepo;
    private final ChequeRepo chequeRepo;
    
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
                    .salesInvoice(new TempInvoiceEntity(paymentsDto.getSalesInvoice()))
                    .build();
            var savedPaymentEntity =  paymentsRepo.save(aPayment);
            addDetailsToTheRelevantPayMethod(paymentsDto, savedPaymentEntity);
            Optional<TempInvoiceEntity> selectedSalesInvoice = tempInvoiceRepo.findById(paymentsDto.getSalesInvoice().getTempInvoiceId());
            selectedSalesInvoice.get().setPaidAmount(selectedSalesInvoice.get().getPaidAmount() + paymentsDto.getPaidAmount());
            tempInvoiceRepo.save(selectedSalesInvoice.get());

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

    private void addDetailsToTheRelevantPayMethod(PaymentsDto paymentsDto, PaymentsEntity savedPayment) {

        if(paymentsDto.getPaymentType().equalsIgnoreCase("card")){
            CardEntity aCardPayment = CardEntity.builder()
                    .cardRefNo(paymentsDto.getCardRefNo())
                    .paidAmount(paymentsDto.getPaidAmount())
                    .paidDate(paymentsDto.getPaidDate())
                    .paymentId(savedPayment.getPaymentId())
                    .tempInvoiceEntity(savedPayment.getSalesInvoice())
                    .build();
            cardRepo.save(aCardPayment);
        } else if (paymentsDto.getPaymentType().equalsIgnoreCase("cash")) {
            CashEntity aCashPayment = CashEntity.builder()
                    .paidAmount(paymentsDto.getPaidAmount())
                    .paidDate(paymentsDto.getPaidDate())
                    .paymentId(savedPayment.getPaymentId())
                    .tempInvoiceEntity(savedPayment.getSalesInvoice())
                    .build();
            cashRepo.save(aCashPayment);
        }else if (paymentsDto.getPaymentType().equalsIgnoreCase("cheque")){
            ChequeEntity aChequePayment = ChequeEntity.builder()
                    .paidAmount(paymentsDto.getPaidAmount())
                    .paidDate(paymentsDto.getPaidDate())
                    .chequeRefNo(paymentsDto.getChequeRefNo())
                    .paymentId(savedPayment.getPaymentId())
                    .chequeDueDate(paymentsDto.getChequeDueDate())
                    .tempInvoiceEntity(savedPayment.getSalesInvoice())
                    .build();
            chequeRepo.save(aChequePayment);
        }
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
            List<PaymentsEntity> paymentsEntities = paymentsRepo.findBySalesInvoice_TempInvoiceId(invoiceId);
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
    public NonPaginatedResponse selectA_Payment() {return null;}
}
