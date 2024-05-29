package com.appli.clcapi.confirmInvoice.serviceImple;

import com.appli.clcapi.common.constants.ConfirmInvoiceConsonants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmInvoiceRepo;
import com.appli.clcapi.confirmInvoice.service.ConfirmInvoiceService;
import com.appli.clcapi.confirmInvoice.confirmCartItems.service.ConfirmProductCartService;
import com.appli.clcapi.payments.entity.PaymentsEntity;
import com.appli.clcapi.payments.paymentMethod.entity.CardEntity;
import com.appli.clcapi.payments.paymentMethod.repository.CardRepo;
import com.appli.clcapi.payments.paymentMethod.repository.CashRepo;
import com.appli.clcapi.payments.paymentMethod.repository.ChequeRepo;
import com.appli.clcapi.payments.repository.PaymentsRepo;
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
    private final PaymentsRepo paymentsRepo;
    private final CardRepo cardRepo;
    private final CashRepo cashRepo;
    private final ChequeRepo chequeRepo;


    @Override
    @Transactional
    public NonPaginatedResponse insertIntoConfirmInvoice(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            TempInvoiceEntity selectedTempInvoice = tempInvoiceRepo.findById(invoiceId).get();

            ConfirmInvoiceEntity confirmedInvoice = createNewConfirmInvoiceData(selectedTempInvoice);
            Boolean isCartItemsConfirmed = confirmProductCartService.confirmTheCartItems(invoiceId, confirmedInvoice);
            if (isCartItemsConfirmed) {
                List<PaymentsEntity> selectAllPayments = paymentsRepo.findBySalesInvoice_TempInvoiceId(confirmedInvoice.getConfirmInvoiceId());
                List<PaymentsEntity> listOfPayments = selectAllPayments.stream().map(
                        aPayment -> {
                            PaymentsEntity aPay = new PaymentsEntity();
                            aPay.setPaymentType(aPayment.getPaymentType());
                            aPay.setPaidDate(aPayment.getPaidDate());
                            aPay.setPaidAmount(aPayment.getPaidAmount());
                            aPay.setConfirmInvoice(confirmedInvoice);
                            aPay.setSalesInvoice(null);
                            alterPaymentMethConfirmInvoice(aPayment, confirmedInvoice);

                            return aPay;
                        }
                ).toList();

                paymentsRepo.saveAll(listOfPayments);
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


    public void alterPaymentMethConfirmInvoice(PaymentsEntity aPayment, ConfirmInvoiceEntity confirmedInvoice) {

        if (aPayment.getPaymentType().equalsIgnoreCase("card")) {
            List<CardEntity> cardEntities = cardRepo.findByTempInvoiceEntity_TempInvoiceId(confirmedInvoice.getConfirmInvoiceId());
            for (CardEntity cardEntity : cardEntities) {
                cardEntity.setConfirmInvoiceEntity(confirmedInvoice);
                cardEntity.setTempInvoiceEntity(null);
            }
            cardRepo.saveAll(cardEntities);
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
        return confirmInvoiceRepo.save(confirmInvoiceEntity);

    }


}
