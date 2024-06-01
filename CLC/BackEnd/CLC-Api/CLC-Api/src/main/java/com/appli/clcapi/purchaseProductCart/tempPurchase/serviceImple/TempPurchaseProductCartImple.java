package com.appli.clcapi.purchaseProductCart.tempPurchase.serviceImple;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.purchase.repository.TempPurchaseRepo;
import com.appli.clcapi.purchaseProductCart.tempPurchase.dto.TempPurchaseProductCartDto;
import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import com.appli.clcapi.purchaseProductCart.tempPurchase.repository.TempPurchaseProductCartRepo;
import com.appli.clcapi.purchaseProductCart.tempPurchase.service.TempPurchaseProductCartService;
import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.stock.repository.StockRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TempPurchaseProductCartImple implements TempPurchaseProductCartService {
    private final TempPurchaseProductCartRepo tempPurchaseProductCartRepo;
    private final TempPurchaseRepo tempPurchaseRepo;
    private final StockRepo stockRepo;

    @Override
    public NonPaginatedResponse addToTempPurchaseCart(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{

           Optional<TempPurchaseEntity> selectedTempPurchaseInvoice =  tempPurchaseRepo.findById(tempPurchaseProductCartDto.getProductCartId());
           Optional<StockEntity> selectedStockItem = stockRepo.findById(tempPurchaseProductCartDto.getStockDto().getStockId());
            if (selectedStockItem.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Stock isn't exist!"));
                return response;
            }
            if (selectedTempPurchaseInvoice.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Purchase Invoice isn't exist!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getQuantity() == 0 || tempPurchaseProductCartDto.getQuantity() < 0) {
                response.setErrors(List.of("Quantity can't be neither 0 nor less!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;

            }
            createPurchaseCartRecord(tempPurchaseProductCartDto);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Product is successfully Added into the Cart!");
        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't add the product into the Cart!"));
        }
        return response;
    }

    private void createPurchaseCartRecord(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        TempPurchaseProductCartEntity newPurchaseCartRecord = TempPurchaseProductCartEntity.builder()
                .productCartId(tempPurchaseProductCartDto.getProductCartId())
                .quantity(tempPurchaseProductCartDto.getQuantity())
                .discount(tempPurchaseProductCartDto.getDiscount())
                .total(tempPurchaseProductCartDto.getTotal())
                .netAmount(tempPurchaseProductCartDto.getNetAmount())
                .stockEntity(new StockEntity(tempPurchaseProductCartDto.getStockDto()))
                .tempPurchaseEntity(new TempPurchaseEntity(tempPurchaseProductCartDto.getTempPurchaseEntity()))
                .build();
        tempPurchaseProductCartRepo.save(newPurchaseCartRecord);
    }

    @Override
    public NonPaginatedResponse deleteTempPurchaseCartRecord(Long proCartId) {
        return null;
    }

    @Override
    public NonPaginatedResponse getAllTempPurchaseCartItems() {
        return null;
    }

    @Override
    public NonPaginatedResponse selectTempPurchaseRecords(String exitingChar) {
        return null;
    }

    @Override
    public NonPaginatedResponse updateTempPurchaseCartRecord(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        return null;
    }
}
