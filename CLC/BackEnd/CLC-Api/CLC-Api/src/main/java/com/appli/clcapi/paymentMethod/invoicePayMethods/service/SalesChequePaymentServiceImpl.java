package com.appli.clcapi.paymentMethod.invoicePayMethods.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;

import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.dto.ConfirmChequeDto;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmChequeRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.dto.TempChequeDto;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository.TempChequeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SalesChequePaymentServiceImpl implements SalesChequePaymentsService {

    private final TempChequeRepo tempChequeRepo;
    private final ConfirmChequeRepo confirmChequeRepo;

    @Override
    public NonPaginatedResponse getAllConfirmedSalesInvoiceDueCheques() {
         NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmChequeEntity> confirmChequeList = confirmChequeRepo.findAll();

            LocalDateTime today = LocalDateTime.now();
            int alertThreshold = 5;
            List<ConfirmChequeDto> dueCheques = new ArrayList<>();
            for (ConfirmChequeEntity cheque : confirmChequeList) {
                long daysRemaining = cheque.getChequeDueDate().toLocalDate().toEpochDay() - today.toLocalDate().toEpochDay();
                if (daysRemaining <= alertThreshold && daysRemaining >= 0) {
                    ConfirmChequeDto aChequeDto = new ConfirmChequeDto(cheque);
                    dueCheques.add(aChequeDto);
                }
            }

            if (dueCheques.isEmpty()){
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setErrors(List.of("No Confirmed SalesInvoice Due Cheques found!"));
                return response;
            }
            response.setResult(dueCheques);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Confirmed SalesInvoice Due Cheques are retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve Due Cheques!"));
        }
        return response;
    }

    @Override
    public NonPaginatedResponse getAllTempSalesInvoiceDueCheques() {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {
            List<TempChequeEntity> tempChequeList = tempChequeRepo.findAll();

            LocalDateTime today = LocalDateTime.now();
            int alertThreshold = 5;
            List<TempChequeDto> dueCheques = new ArrayList<>();
            for (TempChequeEntity cheque : tempChequeList) {
                long daysRemaining = cheque.getChequeDueDate().toLocalDate().toEpochDay() - today.toLocalDate().toEpochDay();
                if (daysRemaining <= alertThreshold && daysRemaining >= 0) {
                    TempChequeDto aChequeDto = new TempChequeDto(cheque);
                    dueCheques.add(aChequeDto);
                }
            }

            if (dueCheques.isEmpty()){
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setErrors(List.of("No Temp Sales Invoice Due Cheques found!"));
                return response;
            }
            response.setResult(dueCheques);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Temp Sales Invoice Due Cheques are retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve Due Cheques!"));
        }
        return response;
    }
}
