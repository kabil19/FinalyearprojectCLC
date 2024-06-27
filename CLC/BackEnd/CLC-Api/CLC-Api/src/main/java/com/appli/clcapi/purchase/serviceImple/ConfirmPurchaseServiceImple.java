package com.appli.clcapi.purchase.serviceImple;

import com.appli.clcapi.common.constants.ConfirmPurchaseConsonants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.purchase.dto.ConfirmPurchaseDto;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseProductCartEntity;
import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.purchase.repository.ConfirmPurchaseProductCartRepo;
import com.appli.clcapi.purchase.repository.ConfirmPurchaseRepo;
import com.appli.clcapi.purchase.repository.TempPurchaseRepo;
import com.appli.clcapi.purchase.service.ConfirmPurchaseService;
import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import com.appli.clcapi.purchaseProductCart.tempPurchase.repository.TempPurchaseProductCartRepo;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.stock.repository.StockRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ConfirmPurchaseServiceImple implements ConfirmPurchaseService {
    private final TempPurchaseRepo tempPurchaseRepo;
    private final ConfirmPurchaseRepo confirmPurchaseRepo;

    private final ConfirmPurchaseProductCartRepo confirmPurchaseProductCartRepo;
    private final TempPurchaseProductCartRepo tempPurchaseProductCartRepo;
    private final StockRepo stockRepo;

    @Override
    @Transactional
    public NonPaginatedResponse addToConfirmThePurchase(Long purchaseId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {

            Optional<TempPurchaseEntity> selectTempPurchase = tempPurchaseRepo.findById(purchaseId);
            if (selectTempPurchase.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Purchase isn't exist!"));
                return response;
            }

            ConfirmPurchaseEntity confirmPurchaseRecord = createConfirmPurchase(purchaseId, selectTempPurchase);
            if (isConfirmPurchaseCartCreated(confirmPurchaseRecord)) {
                tempPurchaseRepo.deleteById(purchaseId);
                response.setStatus(HttpStatus.ACCEPTED);
                response.setSuccessMessage(ConfirmPurchaseConsonants.PURCHASE_HAS_BEEN_CONFIRMED);
            }


        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't confirm the purchase"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private boolean isConfirmPurchaseCartCreated(ConfirmPurchaseEntity confirmPurchaseEntity) {

        try {
            List<TempPurchaseProductCartEntity> tempPurchaseProductCartEntity = tempPurchaseProductCartRepo.
                    findByTempPurchaseEntity_PurchaseId(confirmPurchaseEntity.getConfirmPurchaseId());
            if (!tempPurchaseProductCartEntity.isEmpty()) {
                for (TempPurchaseProductCartEntity aTemp : tempPurchaseProductCartEntity) {
                    ConfirmPurchaseProductCartEntity aConfirmRec = ConfirmPurchaseProductCartEntity.builder()
                            .productCartId(aTemp.getProductCartId())
                            .quantity(aTemp.getQuantity())
                            .confirmPurchaseEntity(confirmPurchaseEntity)
                            .grossAmount(aTemp.getGrossAmount())
                            .purchasePrice(aTemp.getPurchasePrice())
                            .sellingPrice(aTemp.getSellingPrice())
                            .netAmount(aTemp.getNetAmount())
                            .stockEntity(aTemp.getStockEntity())
                            .discount(aTemp.getDiscount())
                            .build();
                    ConfirmPurchaseProductCartEntity anItem = confirmPurchaseProductCartRepo.save(aConfirmRec);
                    confirmPurchaseProductCartRepo.save(anItem);
                }
                List<ConfirmPurchaseProductCartEntity> confirmedProductsCartItems = confirmPurchaseProductCartRepo.
                        findByConfirmPurchaseEntity_ConfirmPurchaseId(confirmPurchaseEntity.getConfirmPurchaseId());
                alterStockItem(confirmedProductsCartItems);
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void alterStockItem(List<ConfirmPurchaseProductCartEntity> confirmedProductsCartItems) {
        for (ConfirmPurchaseProductCartEntity cartItem : confirmedProductsCartItems) {
            Optional<StockEntity> stockItem = stockRepo.findById(cartItem.getStockEntity().getStockId());
            if (stockItem.isPresent()) {
                if (stockItem.get().getSellingPrice() < cartItem.getSellingPrice()) {
                    stockItem.get().setSellingPrice(cartItem.getSellingPrice());
                    stockItem.get().setPurchasePrice(cartItem.getPurchasePrice());
                    stockItem.get().setQuantity(stockItem.get().getQuantity() + cartItem.getQuantity());
                    stockRepo.save(stockItem.get());
                }
            }

        }
    }
    private ConfirmPurchaseEntity createConfirmPurchase(Long purchaseId, Optional<TempPurchaseEntity> selectTempPurchase) {
        //       have to find the total, after adding the confirmPurchaseCart,

        if(selectTempPurchase.isPresent()){
            ConfirmPurchaseEntity newConfirmPurchase = ConfirmPurchaseEntity.builder()
                    .confirmPurchaseId(purchaseId)
                    .purchaseInvoice(selectTempPurchase.get().getPurchaseInvoiceNO())
                    .purchaseDate(LocalDateTime.now())
                    .isComplete(false)
                    .vendorEntity(selectTempPurchase.get().getVendorEntity())
                    .paidAmount(0.0)
                    .netAmount(selectTempPurchase.get().getNetAmount())
                    .build();
            return confirmPurchaseRepo.save(newConfirmPurchase);
        }else {
            return null;
        }

    }

    @Override
    public NonPaginatedResponse getAllConfirmPurchaseInvoices() {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmPurchaseEntity> confirmedPurchaseEntities = confirmPurchaseRepo.findAll();
            List<ConfirmPurchaseDto> confirmedPurchases = confirmedPurchaseEntities.stream()
                    .filter(ConfirmPurchaseEntity -> !ConfirmPurchaseEntity.getIsComplete())
                    .map(ConfirmPurchaseDto::new)
                    .toList();
            response.setResult(confirmedPurchases);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Confirmed Purchase invoice records Retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve purchase records!"));
        }
        return response;
    }

    public NonPaginatedResponse searchConfirmPurchaseInvoices(String characters) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmPurchaseEntity> confirmedPurchaseEntities = confirmPurchaseRepo.searchByVendorNameOrPurchaseInvoiceOrPurchaseDate(characters);
            List<ConfirmPurchaseDto> confirmedPurchases = confirmedPurchaseEntities.stream()
                    .filter(ConfirmPurchaseEntity -> !ConfirmPurchaseEntity.getIsComplete())
                    .map(ConfirmPurchaseDto::new)
                    .toList();
            response.setResult(confirmedPurchases);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Selected Confirmed Purchase invoice records Retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve the selected purchase records!"));
        }
        return response;
    }


}
