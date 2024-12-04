package com.appli.clcapi.confirmInvoice.serviceImple;

import com.appli.clcapi.common.constants.ConfirmInvoiceConsonants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmSalesInvoiceRepo;
import com.appli.clcapi.confirmInvoice.service.ConfirmInvoiceService;
import com.appli.clcapi.confirmInvoice.confirmCartItems.service.ConfirmProductCartService;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmSalesInvoiceChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmCardRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmCashRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmSalesPayChequeRepo;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmSalesPaymentsEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.repository.ConfirmSalesPaymentsRepo;
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

import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class ConfirmInvoiceServiceImple implements ConfirmInvoiceService {

    private final TempInvoiceRepo tempInvoiceRepo;
    private final ConfirmSalesInvoiceRepo confirmSalesInvoiceRepo;
    private final ConfirmProductCartService confirmProductCartService;

    //    PaymentRepos to set the ConfirmInvoiceId (FK)
    private final TempPaymentsRepo tempPaymentsRepo;
    private final TempCardRepo tempCardRepo;
    private final TempCashRepo tempCashRepo;
    private final TempChequeRepo tempChequeRepo;
    private final ConfirmSalesPaymentsRepo confirmSalesPaymentsRepo;
    private final ConfirmCardRepo confirmCardRepo;
    private final ConfirmCashRepo confirmCashRepo;
    private final ConfirmSalesPayChequeRepo confirmSalesPayChequeRepo;


    @Override
    @Transactional
    public NonPaginatedResponse insertIntoConfirmInvoice(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<TempInvoiceEntity> selectedTempInvoice = tempInvoiceRepo.findById(invoiceId);
            if(selectedTempInvoice.isEmpty()){
                response.setErrors(List.of("Temporary Sales Invoice isn't exist!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            ConfirmSalesInvoiceEntity confirmedInvoice = createNewConfirmInvoiceData(selectedTempInvoice.get());
            Boolean isCartItemsConfirmed = confirmProductCartService.confirmTheCartItems(invoiceId, confirmedInvoice);
            if (isCartItemsConfirmed) {
                List<TempPaymentsEntity> selectAllPayments = tempPaymentsRepo.findByTempSalesInvoice_TempInvoiceId(confirmedInvoice.getConfirmInvoiceId());
                List<ConfirmSalesPaymentsEntity> listOfPayments = selectAllPayments.stream().map(
                        aPayment -> {
                            ConfirmSalesPaymentsEntity aPay = new ConfirmSalesPaymentsEntity();
                            aPay.setPaymentType(aPayment.getPaymentType());
                            aPay.setPaidDate(aPayment.getPaidDate());
                            aPay.setPaidAmount(aPayment.getPaidAmount());
                            aPay.setConfirmSalesInvoiceEntity(confirmedInvoice);
                            return aPay;
                        }
                ).toList();
                confirmSalesPaymentsRepo.saveAll(listOfPayments);
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
            response.setErrors(List.of("Couldn't Confirm the selected invoice"));
            response.setStatus(HttpStatus.BAD_GATEWAY);
        }
        return response;
    }




    private boolean transferToConfirmPayMethods(List<TempCardEntity> tempCardEntities, List<TempCashEntity> tempCashEntities, List<TempChequeEntity> tempChequeEntities, ConfirmSalesInvoiceEntity confirmedInvoice) {
        try {
            if (!tempCardEntities.isEmpty()) {
                List<ConfirmCardEntity> confirmCardList = tempCardEntities.stream()
                        .map(cardEntityData -> {
                            ConfirmCardEntity aCard = new ConfirmCardEntity();
                            aCard.setCardRefNo(cardEntityData.getCardRefNo());
                            aCard.setConfirmSalesInvoiceEntity(confirmedInvoice);
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
                            aCash.setConfirmSalesInvoiceEntity(confirmedInvoice);
                            return aCash;
                        }).toList();
                confirmCashRepo.saveAll(confirmCashList);
            }
            if (!tempChequeEntities.isEmpty()) {
                List<ConfirmSalesInvoiceChequeEntity> confirmChequeList = tempChequeEntities.stream()
                        .map(chequeEntity -> {
                            ConfirmSalesInvoiceChequeEntity aCheque = new ConfirmSalesInvoiceChequeEntity();
                            aCheque.setChequeDueDate(chequeEntity.getChequeDueDate());
                            aCheque.setChequeRefNo(chequeEntity.getChequeRefNo());
                            aCheque.setPaidAmount(chequeEntity.getPaidAmount());
                            aCheque.setPaidDate(chequeEntity.getPaidDate());
                            aCheque.setPaymentId(chequeEntity.getPaymentId());
                            aCheque.setConfirmSalesInvoiceEntity(confirmedInvoice);
                            return aCheque;
                        }).toList();
                confirmSalesPayChequeRepo.saveAll(confirmChequeList);
            }


            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }


    private ConfirmSalesInvoiceEntity createNewConfirmInvoiceData(TempInvoiceEntity tempInvoiceEntity) {

        ConfirmSalesInvoiceEntity confirmSalesInvoiceEntity = new ConfirmSalesInvoiceEntity();
        confirmSalesInvoiceEntity.setConfirmInvoiceId(tempInvoiceEntity.getTempInvoiceId());
        confirmSalesInvoiceEntity.setInvoiceReference(tempInvoiceEntity.getTempInvoiceNumberReference());
        confirmSalesInvoiceEntity.setDate(LocalDateTime.now());
        confirmSalesInvoiceEntity.setPaidAmount(tempInvoiceEntity.getPaidAmount());
        confirmSalesInvoiceEntity.setNetAmount(tempInvoiceEntity.getNetAmount());
        confirmSalesInvoiceEntity.setCustomer(tempInvoiceEntity.getCustomer());
        confirmSalesInvoiceEntity.setIsComplete(tempInvoiceEntity.getIsComplete());
        confirmSalesInvoiceEntity.setAdvancePayment(tempInvoiceEntity.getPaidAmount());
        confirmSalesInvoiceEntity.setMainDiscount(tempInvoiceEntity.getMainDiscount());
        confirmSalesInvoiceEntity.setReturnAmount(0.00);
        return confirmSalesInvoiceRepo.save(confirmSalesInvoiceEntity);
    }

    @Override
    public NonPaginatedResponse getAllConfirmedInvoices() {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmSalesInvoiceEntity> confirmedSalesInvoiceEntities = confirmSalesInvoiceRepo.findAll();
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

    public NonPaginatedResponse getConfirmedInvoiceByInvoiceNumber(String invoiceNo){
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
           ConfirmSalesInvoiceEntity confirmedSalesInvoiceEntity = confirmSalesInvoiceRepo.findByInvoiceReference(invoiceNo);
            ConfirmInvoiceDto confirmedSalesInvoice = new ConfirmInvoiceDto(confirmedSalesInvoiceEntity);
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
            List<ConfirmSalesInvoiceEntity> confirmedSalesInvoiceEntities = confirmSalesInvoiceRepo.searchByCustomerNameOrInvoiceNoOrInvoiceDate(characters);
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
