package com.appli.clcapi.purchase.serviceImple;

import com.appli.clcapi.common.constants.ConfirmPurchaseConsonants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayCardRepo;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayCashRepo;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayChequeRepo;
import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import com.appli.clcapi.payments.purchasePayment.repository.PurchasePaymentRepo;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.purchase.repository.ConfirmPurchaseRepo;
import com.appli.clcapi.purchase.repository.TempPurchaseRepo;
import com.appli.clcapi.purchase.service.ConfirmPurchaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConfirmPurchaseServiceImple implements ConfirmPurchaseService {
    private final TempPurchaseRepo tempPurchaseRepo;
    private final ConfirmPurchaseRepo confirmPurchaseRepo;
    private final PurchasePaymentRepo purchasePaymentRepo;
    private final PurchasePayCardRepo purchasePayCardRepo;
    private final PurchasePayCashRepo purchasePayCashRepo;
    private final PurchasePayChequeRepo purchasePayChequeRepo;
    @Override
    @Transactional
    public NonPaginatedResponse addToConfirmThePurchase(Long purchaseId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            Optional<TempPurchaseEntity> selectTempPurchase = getTempPurchaseEntity(purchaseId);
            Optional<PurchasePaymentEntity> selectPayments = getPurchasePayments(purchaseId);
            if (selectTempPurchase.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Purchase isn't exist!"));
                return response;
            }
            if(createConfirmPurchase(purchaseId, selectTempPurchase, selectPayments)){
                response.setStatus(HttpStatus.ACCEPTED);
                response.setSuccessMessage(ConfirmPurchaseConsonants.PURCHASE_HAS_BEEN_CONFIRMED);
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setErrors(List.of("Couldn't confirm the purchase"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private Boolean createConfirmPurchase(Long purchaseId, Optional<TempPurchaseEntity> selectTempPurchase, Optional<PurchasePaymentEntity> selectPayments) {
        //       have to find the total, after adding the confirmPurchaseCart,
        Date now = new Date();
        ConfirmPurchaseEntity newConfirmPurchase = ConfirmPurchaseEntity.builder()
                .confirmPurchaseId(purchaseId)
                .purchaseInvoice(selectTempPurchase.get().getPurchaseInvoiceNO())
                .purchaseDate(now)
                .vendorEntity(selectTempPurchase.get().getVendorEntity())
                .paidAmount(selectPayments.get().getPaidAmount())
                .totalAmount(selectTempPurchase.get().getTotalAmount())
                .build();
        confirmPurchaseRepo.save(newConfirmPurchase);
        return true;
    }

    private Optional<TempPurchaseEntity> getTempPurchaseEntity(Long purchaseId) {
        return tempPurchaseRepo.findById(purchaseId);
    }
    private Optional<PurchasePaymentEntity> getPurchasePayments(Long purchaseId) {
        return purchasePaymentRepo.findById(purchaseId);
    }


}
