package com.appli.clcapi.payments.invoicePayments.tempPayments.serviceImple;

import com.appli.clcapi.common.constants.PaymentsConstants;
import com.appli.clcapi.common.response.NonPaginatedResponse;

import com.appli.clcapi.payments.invoicePayments.tempPayments.dto.TempPaymentsDto;
import com.appli.clcapi.payments.invoicePayments.tempPayments.entity.TempPaymentsEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository.TempCardRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository.TempCashRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository.TempChequeRepo;
import com.appli.clcapi.payments.invoicePayments.tempPayments.repository.TempPaymentsRepo;
import com.appli.clcapi.payments.invoicePayments.tempPayments.service.TempPaymentsService;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TempPaymentsImple implements TempPaymentsService {

    private final TempPaymentsRepo tempPaymentsRepo;
    private final TempInvoiceRepo tempInvoiceRepo;
    private final TempCardRepo tempCardRepo;
    private final TempCashRepo tempCashRepo;
    private final TempChequeRepo tempChequeRepo;


    @Override
    @Transactional
    public NonPaginatedResponse addPayment(TempPaymentsDto tempPaymentsDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<TempInvoiceEntity> tempInvoiceEntity = tempInvoiceRepo.findById(tempPaymentsDto.getTempSalesInvoice().getTempInvoiceId());
            if ((tempInvoiceEntity.get().getPaidAmount()) + tempPaymentsDto.getPaidAmount() > tempInvoiceEntity.get().getNetAmount()) {
                response.setErrors(List.of("Payment exceeds the total!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            NonPaginatedResponse paymentSourceStatus = checkPaymentSource(tempPaymentsDto, response);
            if(paymentSourceStatus.getStatus().isSameCodeAs(HttpStatus.BAD_REQUEST)){
                return paymentSourceStatus;
            }
            TempPaymentsEntity aPayment = TempPaymentsEntity.builder()
                    .paymentType(tempPaymentsDto.getPaymentType())
                    .paidAmount(tempPaymentsDto.getPaidAmount())
                    .paidDate(new Date())
                    .tempSalesInvoice(new TempInvoiceEntity(tempPaymentsDto.getTempSalesInvoice()))
                    .build();
            var savedPaymentEntity = tempPaymentsRepo.save(aPayment);

            addDetailsToTheRelevantPayMethod(tempPaymentsDto, savedPaymentEntity);

            Optional<TempInvoiceEntity> selectedSalesInvoice = tempInvoiceRepo.findById(tempPaymentsDto.getTempSalesInvoice().getTempInvoiceId());
            double totalPaidAmount = tempPaymentsDto.getPaidAmount() + selectedSalesInvoice.get().getPaidAmount();
            selectedSalesInvoice.get().setPaidAmount(totalPaidAmount);
            if (totalPaidAmount == (selectedSalesInvoice.get().getNetAmount())) {
                selectedSalesInvoice.get().setIsComplete(true);

            } else {
                selectedSalesInvoice.get().setIsComplete(false);
            }
            tempInvoiceRepo.save(selectedSalesInvoice.get());


            response.setResult(tempPaymentsDto);
            response.setSuccessMessage(PaymentsConstants.PAYMENT_HAS_BEEN_ADDED);
            response.setStatus(HttpStatus.CREATED);

        } catch (Exception e) {
            response.setSuccessMessage(null);
//            response.setErrors(Arrays.asList("An error occurred"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private NonPaginatedResponse checkPaymentSource(TempPaymentsDto tempPaymentsDto, NonPaginatedResponse response){
        if (tempPaymentsDto.getPaymentType().equalsIgnoreCase("card")) {
            boolean isCardExists = tempCardRepo.existsById(tempPaymentsDto.getCardRefNo());
            if (isCardExists) {
                response.setErrors(List.of("Card ref No. already exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        if (tempPaymentsDto.getPaymentType().equalsIgnoreCase("cheque")) {
            boolean isChequeExists = tempChequeRepo.existsById(tempPaymentsDto.getChequeRefNo());
            if (isChequeExists) {
                response.setErrors(List.of("Cheque ref No. already exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        response.setStatus(HttpStatus.OK);
        return response;
    }

    private void addDetailsToTheRelevantPayMethod(TempPaymentsDto tempPaymentsDto, TempPaymentsEntity savedPayment) {
        Date now = new Date();
        if (tempPaymentsDto.getPaymentType().equalsIgnoreCase("card")) {
            TempCardEntity aCardPayment = TempCardEntity.builder()
                    .cardRefNo(tempPaymentsDto.getCardRefNo())
                    .paidAmount(tempPaymentsDto.getPaidAmount())
                    .paidDate(now)
                    .paymentId(savedPayment.getPaymentId())
                    .tempInvoiceEntity(savedPayment.getTempSalesInvoice())
                    .build();
            tempCardRepo.save(aCardPayment);
        }
        if (tempPaymentsDto.getPaymentType().equalsIgnoreCase("cash")) {
            TempCashEntity aCashPayment = TempCashEntity.builder()
                    .paidAmount(tempPaymentsDto.getPaidAmount())
                    .paidDate(now)
                    .paymentId(savedPayment.getPaymentId())
                    .tempInvoiceEntity(savedPayment.getTempSalesInvoice())
                    .build();
            tempCashRepo.save(aCashPayment);
        }
        if (tempPaymentsDto.getPaymentType().equalsIgnoreCase("cheque")) {
            TempChequeEntity aChequePayment = TempChequeEntity.builder()
                    .paidAmount(tempPaymentsDto.getPaidAmount())
                    .paidDate(now)
                    .chequeRefNo(tempPaymentsDto.getChequeRefNo())
                    .paymentId(savedPayment.getPaymentId())
                    .chequeDueDate(tempPaymentsDto.getChequeDueDate())
                    .tempInvoiceEntity(savedPayment.getTempSalesInvoice())
                    .build();
            tempChequeRepo.save(aChequePayment);
        }

    }

    @Override
    public NonPaginatedResponse deletePayment(Long payId) {
        return null;
    }

    @Override
    public NonPaginatedResponse updatePayment(TempPaymentsDto paymentsDto) {
        return null;
    }

    @Override
    public NonPaginatedResponse getAllPayments(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<TempPaymentsEntity> paymentsEntities = tempPaymentsRepo.findByTempSalesInvoice_TempInvoiceId(invoiceId);
            List<TempPaymentsDto> paymentsDtos = new ArrayList<>();
            for (TempPaymentsEntity aPay : paymentsEntities) {
                TempPaymentsDto aPaymentDto = new TempPaymentsDto(aPay);
                paymentsDtos.add(aPaymentDto);
            }
            response.setResult(paymentsDtos);
            response.setStatus(HttpStatus.OK);
            response.setSuccessMessage("Data is retrieved");
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't find anything"));
        }
        return response;
    }

    @Override
    public NonPaginatedResponse selectA_Payment() {
        return null;
    }
}
