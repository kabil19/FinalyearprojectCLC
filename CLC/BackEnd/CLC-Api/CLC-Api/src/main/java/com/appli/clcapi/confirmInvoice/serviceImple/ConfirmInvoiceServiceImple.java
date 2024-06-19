package com.appli.clcapi.confirmInvoice.serviceImple;

import com.appli.clcapi.common.constants.ConfirmInvoiceConsonants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmInvoiceRepo;
import com.appli.clcapi.confirmInvoice.service.ConfirmInvoiceService;
import com.appli.clcapi.confirmInvoice.confirmCartItems.service.ConfirmProductCartService;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmCardRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmCashRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmChequeRepo;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmPaymentsEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.repository.ConfirmPaymentsRepo;
import com.appli.clcapi.payments.invoicePayments.tempPayments.entity.TempPaymentsEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository.TempCardRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository.TempCashRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository.TempChequeRepo;
import com.appli.clcapi.payments.invoicePayments.tempPayments.repository.TempPaymentsRepo;

import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class ConfirmInvoiceServiceImple implements ConfirmInvoiceService {

    private final TempInvoiceRepo tempInvoiceRepo;
    private final ConfirmInvoiceRepo confirmInvoiceRepo;
    private final ConfirmProductCartService confirmProductCartService;

    //    PaymentRepos to set the ConfirmInvoiceId (FK)
    private final TempPaymentsRepo tempPaymentsRepo;
    private final TempCardRepo tempCardRepo;
    private final TempCashRepo tempCashRepo;
    private final TempChequeRepo tempChequeRepo;
    private final ConfirmPaymentsRepo confirmPaymentsRepo;
    private final ConfirmCardRepo confirmCardRepo;
    private final ConfirmCashRepo confirmCashRepo;
    private final ConfirmChequeRepo confirmChequeRepo;


    @Override
    @Transactional
    public NonPaginatedResponse insertIntoConfirmInvoice(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            TempInvoiceEntity selectedTempInvoice = tempInvoiceRepo.findById(invoiceId).get();
            ConfirmInvoiceEntity confirmedInvoice = createNewConfirmInvoiceData(selectedTempInvoice);
            Boolean isCartItemsConfirmed = confirmProductCartService.confirmTheCartItems(invoiceId, confirmedInvoice);
            if (isCartItemsConfirmed) {
                List<TempPaymentsEntity> selectAllPayments = tempPaymentsRepo.findByTempSalesInvoice_TempInvoiceId(confirmedInvoice.getConfirmInvoiceId());
                List<ConfirmPaymentsEntity> listOfPayments = selectAllPayments.stream().map(
                        aPayment -> {
                            ConfirmPaymentsEntity aPay = new ConfirmPaymentsEntity();
                            aPay.setPaymentType(aPayment.getPaymentType());
                            aPay.setPaidDate(aPayment.getPaidDate());
                            aPay.setPaidAmount(aPayment.getPaidAmount());
                            aPay.setConfirmInvoice(confirmedInvoice);
                            return aPay;
                        }
                ).toList();
                confirmPaymentsRepo.saveAll(listOfPayments);
                List<TempCardEntity> tempCardEntities = tempCardRepo.findByTempInvoiceEntity_TempInvoiceId(confirmedInvoice.getConfirmInvoiceId());
                List<TempCashEntity> tempCashEntities = tempCashRepo.findByTempInvoiceEntity_TempInvoiceId(confirmedInvoice.getConfirmInvoiceId());
                List<TempChequeEntity> tempChequeEntities = tempChequeRepo.findByTempInvoiceEntity_TempInvoiceId(confirmedInvoice.getConfirmInvoiceId());

                if (!transferToConfirmPayMethods(tempCardEntities, tempCashEntities, tempChequeEntities, confirmedInvoice)) {
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    return response;
                }
                tempInvoiceRepo.deleteById(confirmedInvoice.getConfirmInvoiceId());

            }
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage(ConfirmInvoiceConsonants.INVOICE_HAS_BEEN_CONFIRMED);
            response.setResult(null);
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(Arrays.asList("Couldn't Confirm the selected invoice"));
            response.setStatus(HttpStatus.BAD_GATEWAY);
        }
        return response;
    }




    private boolean transferToConfirmPayMethods(List<TempCardEntity> tempCardEntities, List<TempCashEntity> tempCashEntities, List<TempChequeEntity> tempChequeEntities, ConfirmInvoiceEntity confirmedInvoice) {
        try {
            if (!tempCardEntities.isEmpty()) {
                List<ConfirmCardEntity> confirmCardList = tempCardEntities.stream()
                        .map(cardEntityData -> {
                            ConfirmCardEntity aCard = new ConfirmCardEntity();
                            aCard.setCardRefNo(cardEntityData.getCardRefNo());
                            aCard.setConfirmInvoiceEntity(confirmedInvoice);
                            aCard.setPaidDate(cardEntityData.getPaidDate());
                            aCard.setPaidAmount(cardEntityData.getPaidAmount());
                            aCard.setPaymentId(cardEntityData.getPaymentId());
                            return aCard;
                        }).toList();
                confirmCardRepo.saveAll(confirmCardList);
            }
            if (!tempCashEntities.isEmpty()) {
                List<ConfirmCashEntity> confirmCashList = tempCashEntities.stream()
                        .map(cashEntity -> {
                            ConfirmCashEntity aCash = new ConfirmCashEntity();
                            aCash.setPaidAmount(cashEntity.getPaidAmount());
                            aCash.setPaidDate(cashEntity.getPaidDate());
                            aCash.setPaymentId(cashEntity.getPaymentId());
                            aCash.setConfirmInvoiceEntity(confirmedInvoice);
                            return aCash;
                        }).toList();
                confirmCashRepo.saveAll(confirmCashList);
            }
            if (!tempChequeEntities.isEmpty()) {
                List<ConfirmChequeEntity> confirmChequeList = tempChequeEntities.stream()
                        .map(chequeEntity -> {
                            ConfirmChequeEntity aCheque = new ConfirmChequeEntity();
                            aCheque.setChequeDueDate(chequeEntity.getChequeDueDate());
                            aCheque.setChequeRefNo(chequeEntity.getChequeRefNo());
                            aCheque.setPaidAmount(chequeEntity.getPaidAmount());
                            aCheque.setPaidDate(chequeEntity.getPaidDate());
                            aCheque.setPaymentId(chequeEntity.getPaymentId());
                            aCheque.setConfirmInvoiceEntity(confirmedInvoice);
                            return aCheque;
                        }).toList();
                confirmChequeRepo.saveAll(confirmChequeList);
            }


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    private ConfirmInvoiceEntity createNewConfirmInvoiceData(TempInvoiceEntity tempInvoiceEntity) {

        ConfirmInvoiceEntity confirmInvoiceEntity = new ConfirmInvoiceEntity();
        confirmInvoiceEntity.setConfirmInvoiceId(tempInvoiceEntity.getTempInvoiceId());
        confirmInvoiceEntity.setInvoiceNumber(tempInvoiceEntity.getTempInvoiceNumber());
        confirmInvoiceEntity.setDate(new Date());
        confirmInvoiceEntity.setPaidAmount(tempInvoiceEntity.getPaidAmount());
        confirmInvoiceEntity.setNetAmount(tempInvoiceEntity.getNetAmount());
        confirmInvoiceEntity.setCustomer(tempInvoiceEntity.getCustomer());
        confirmInvoiceEntity.setIsComplete(tempInvoiceEntity.getIsComplete());
        return confirmInvoiceRepo.save(confirmInvoiceEntity);
    }

    @Override
    public NonPaginatedResponse getAllConfirmedInvoices() {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmInvoiceEntity> confirmedSalesInvoiceEntities =confirmInvoiceRepo.findAll();
            List<ConfirmInvoiceDto> confirmedSalesInvoice = confirmedSalesInvoiceEntities.stream()
                    .filter(ConfirmInvoiceEntity -> !ConfirmInvoiceEntity.getIsComplete())
                    .map(ConfirmInvoiceDto::new)
                    .toList();
            response.setResult(confirmedSalesInvoice);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Confirmed Sales invoice records Retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve Sales invoice records!"));
        }
        return response;
    }
    public NonPaginatedResponse searchConfirmedSalesInvoice(String characters){
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmInvoiceEntity> confirmedSalesInvoiceEntities =confirmInvoiceRepo.searchByCustomerNameOrInvoiceNoOrInvoiceDate(characters);
            List<ConfirmInvoiceDto> confirmedSalesInvoice = confirmedSalesInvoiceEntities.stream()
                    .filter(ConfirmInvoiceEntity -> !ConfirmInvoiceEntity.getIsComplete())
                    .map(ConfirmInvoiceDto::new)
                    .toList();
            response.setResult(confirmedSalesInvoice);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Selected Confirmed Sales invoice records Retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve the selected Sales invoice records!"));
        }
        return response;
    }

}
