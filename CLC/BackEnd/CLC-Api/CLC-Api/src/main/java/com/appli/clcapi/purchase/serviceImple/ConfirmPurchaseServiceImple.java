package com.appli.clcapi.purchase.serviceImple;

import com.appli.clcapi.common.constants.ConfirmPurchaseConsonants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayCardRepo;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayCashRepo;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayChequeRepo;
import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import com.appli.clcapi.payments.purchasePayment.repository.PurchasePaymentRepo;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseProductCartEntity;
import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.purchase.repository.ConfirmPurchaseProductCartRepo;
import com.appli.clcapi.purchase.repository.ConfirmPurchaseRepo;
import com.appli.clcapi.purchase.repository.TempPurchaseRepo;
import com.appli.clcapi.purchase.service.ConfirmPurchaseService;
import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import com.appli.clcapi.purchaseProductCart.tempPurchase.repository.TempPurchaseProductCartRepo;
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

    private final ConfirmPurchaseProductCartRepo confirmPurchaseProductCartRepo;
    private final TempPurchaseProductCartRepo tempPurchaseProductCartRepo;
    @Override
    @Transactional
    public NonPaginatedResponse addToConfirmThePurchase(Long purchaseId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            Optional<TempPurchaseEntity> selectTempPurchase = getTempPurchaseEntity(purchaseId);
            if (selectTempPurchase.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Purchase isn't exist!"));
                return response;
            }

            ConfirmPurchaseEntity confirmPurchaseRecord = createConfirmPurchase(purchaseId, selectTempPurchase);
            isConfirmPurchaseCartCreated(confirmPurchaseRecord);
            tempPurchaseRepo.deleteById(purchaseId);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage(ConfirmPurchaseConsonants.PURCHASE_HAS_BEEN_CONFIRMED);

        }catch (Exception e){
            e.printStackTrace();
            response.setErrors(List.of("Couldn't confirm the purchase"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private boolean isConfirmPurchaseCartCreated(ConfirmPurchaseEntity confirmPurchaseEntity) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            List<TempPurchaseProductCartEntity> tempPurchaseProductCartEntity = tempPurchaseProductCartRepo.
                    findByTempPurchaseEntity_PurchaseId(confirmPurchaseEntity.getConfirmPurchaseId());
            if(!tempPurchaseProductCartEntity.isEmpty()){
                for (TempPurchaseProductCartEntity aTemp : tempPurchaseProductCartEntity) {
                    ConfirmPurchaseProductCartEntity aConfirmRec = ConfirmPurchaseProductCartEntity.builder()
                            .productCartId(aTemp.getProductCartId())
                            .quantity(aTemp.getQuantity())
                            .confirmPurchaseEntity(confirmPurchaseEntity)
                            .grossAmount(aTemp.getGrossAmount())
                            .netAmount(aTemp.getNetAmount())
                            .stockEntity(aTemp.getStockEntity())
                            .discount(aTemp.getDiscount())
                            .build();
                    confirmPurchaseProductCartRepo.save(aConfirmRec);
                }
            }else {
                return false;
            }

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private ConfirmPurchaseEntity createConfirmPurchase(Long purchaseId, Optional<TempPurchaseEntity> selectTempPurchase) {
        //       have to find the total, after adding the confirmPurchaseCart,
        Date now = new Date();
        ConfirmPurchaseEntity newConfirmPurchase = ConfirmPurchaseEntity.builder()
                .confirmPurchaseId(purchaseId)
                .purchaseInvoice(selectTempPurchase.get().getPurchaseInvoiceNO())
                .purchaseDate(now)
                .vendorEntity(selectTempPurchase.get().getVendorEntity())
                .paidAmount(0.0)
                .totalAmount(selectTempPurchase.get().getTotalAmount())
                .build();
        return  confirmPurchaseRepo.save(newConfirmPurchase);

    }

    private Optional<TempPurchaseEntity> getTempPurchaseEntity(Long purchaseId) {
        return tempPurchaseRepo.findById(purchaseId);
    }



}
