package com.appli.clcapi.paymentMethod.purchasePayMethods.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.paymentMethod.purchasePayMethods.dto.PurchasePayChequeDto;
import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayChequeEntity;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayChequeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PurchaseInvoiceChequeServiceImpl implements PurchaseInvoiceChequeService{

    private final PurchasePayChequeRepo purchasePayChequeRepo;
    @Override
    public NonPaginatedResponse getAllConfirmedPurchaseInvoiceDueCheques() {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<PurchasePayChequeEntity> confirmChequeList = purchasePayChequeRepo.findAll();

            LocalDateTime today = LocalDateTime.now();
            int alertThreshold = 5;
            List<PurchasePayChequeDto> dueCheques = new ArrayList<>();
            for (PurchasePayChequeEntity cheque : confirmChequeList) {
                long daysRemaining = cheque.getChequeDueDate().toLocalDate().toEpochDay() - today.toLocalDate().toEpochDay();
                if (daysRemaining <= alertThreshold && daysRemaining >= 0) {
                    PurchasePayChequeDto aChequeDto = new PurchasePayChequeDto(cheque);
                    dueCheques.add(aChequeDto);
                }
            }

            if (dueCheques.isEmpty()){
                response.setStatus(HttpStatus.NOT_FOUND);
                response.setErrors(List.of("No Purchase Invoice Due Cheques found!"));
                return response;
            }
            response.setResult(dueCheques);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Purchase Invoice Due Cheques are retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve Due Cheques!"));
        }
        return response;
    }
}
