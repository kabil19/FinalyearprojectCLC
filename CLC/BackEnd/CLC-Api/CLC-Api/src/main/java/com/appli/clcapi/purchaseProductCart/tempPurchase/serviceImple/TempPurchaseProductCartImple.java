package com.appli.clcapi.purchaseProductCart.tempPurchase.serviceImple;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.purchase.repository.TempPurchaseRepo;
import com.appli.clcapi.purchaseProductCart.tempPurchase.dto.TempPurchaseProductCartDto;
import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import com.appli.clcapi.purchaseProductCart.tempPurchase.repository.TempPurchaseProductCartRepo;
import com.appli.clcapi.purchaseProductCart.tempPurchase.service.TempPurchaseProductCartService;
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
        try {

            Optional<TempPurchaseEntity> selectedTempPurchaseInvoice = tempPurchaseRepo.findById(tempPurchaseProductCartDto.getTempPurchaseEntity().getPurchaseId());
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
            Optional<TempPurchaseProductCartEntity> existingRecordFromTheCart = tempPurchaseProductCartRepo.
                    findByStockEntity_StockIdAndTempPurchaseEntity_PurchaseId(
                            tempPurchaseProductCartDto.getStockDto().getStockId(), tempPurchaseProductCartDto.getTempPurchaseEntity().getPurchaseId()
                    );
            Optional<StockEntity> productFromTheStock = stockRepo.findById(tempPurchaseProductCartDto.getStockDto().getStockId());
            if (existingRecordFromTheCart.isEmpty()) {
                createPurchaseCartRecord(tempPurchaseProductCartDto, productFromTheStock);
            } else {
                addMoreItems(tempPurchaseProductCartDto, productFromTheStock, existingRecordFromTheCart.get(), selectedTempPurchaseInvoice);
            }
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Product is successfully Added into the Cart!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't add the product into the Cart!"));
        }
        return response;
    }

    private void addMoreItems(
            TempPurchaseProductCartDto tempPurchaseProductCartDto,
            Optional<StockEntity> productFromTheStock,
            TempPurchaseProductCartEntity existingProductCartRecord,
            Optional<TempPurchaseEntity> selectedTempPurchaseInvoice) {
        double newNetAmount = alterCartDetailsAndGetNewNetAmount(
                tempPurchaseProductCartDto,
                productFromTheStock,
                existingProductCartRecord
        );
        alterTempPurchase(selectedTempPurchaseInvoice.get(), newNetAmount);
    }

    private double alterCartDetailsAndGetNewNetAmount(TempPurchaseProductCartDto tempPurchaseProductCartDto,
                                                      Optional<StockEntity> productFromTheStock,
                                                      TempPurchaseProductCartEntity existingProductCartRecord) {
        double existingQtyInCart = existingProductCartRecord.getQuantity();
        double newQty = tempPurchaseProductCartDto.getQuantity() + existingQtyInCart;

        double newDiscount = tempPurchaseProductCartDto.getDiscount() + existingProductCartRecord.getDiscount();

        double newTotal = productFromTheStock.get().getSellingPrice() * newQty;
        double newNetAmount = (newTotal) - (newQty * newDiscount);

        existingProductCartRecord.setDiscount(newDiscount);
        existingProductCartRecord.setQuantity(newQty);
        existingProductCartRecord.setGrossAmount(newTotal);
        existingProductCartRecord.setNetAmount(newNetAmount);
        tempPurchaseProductCartRepo.save(existingProductCartRecord);
        return newNetAmount;
    }

    private void alterTempPurchase(TempPurchaseEntity tempPurchaseEntity, double newNetAmount) {
        tempPurchaseEntity.setTotalAmount(newNetAmount);
        tempPurchaseRepo.save(tempPurchaseEntity);
    }


    private void createPurchaseCartRecord(TempPurchaseProductCartDto tempPurchaseProductCartDto, Optional<StockEntity> productFromTheStock) {
        double quantity = tempPurchaseProductCartDto.getQuantity();
        double gross = (productFromTheStock.get().getSellingPrice() * quantity);
        double netAmount = (gross - (quantity * tempPurchaseProductCartDto.getDiscount()));
        TempPurchaseProductCartEntity newPurchaseCartRecord = TempPurchaseProductCartEntity.builder()
                .productCartId(tempPurchaseProductCartDto.getProductCartId())
                .quantity(quantity)
                .discount(tempPurchaseProductCartDto.getDiscount())
                .grossAmount(gross)
                .netAmount(netAmount)
                .stockEntity(new StockEntity(tempPurchaseProductCartDto.getStockDto()))
                .tempPurchaseEntity(new TempPurchaseEntity(tempPurchaseProductCartDto.getTempPurchaseEntity()))
                .build();
        tempPurchaseProductCartRepo.save(newPurchaseCartRecord);
        isNetAmountUpdatedOnTempPurchase(tempPurchaseProductCartDto);
    }

    private Boolean isNetAmountUpdatedOnTempPurchase(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        Optional<TempPurchaseEntity> selectedPurchase = tempPurchaseRepo.findById(tempPurchaseProductCartDto.getTempPurchaseEntity().getPurchaseId());
        if(selectedPurchase.isPresent()){
            selectedPurchase.get().setTotalAmount(selectedPurchase.get().getTotalAmount()+ tempPurchaseProductCartDto.getNetAmount());
            tempPurchaseRepo.save(selectedPurchase.get());
            return true;
        }
        return false;
    }

    @Override
    public NonPaginatedResponse deleteTempPurchaseCartRecord(Long proCartId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<TempPurchaseProductCartEntity> tempPurchaseProductCartEntity = tempPurchaseProductCartRepo.findById(proCartId);
            Optional<TempPurchaseEntity> tempPurchaseEntity = tempPurchaseRepo.findById(tempPurchaseProductCartEntity.get().getTempPurchaseEntity().getPurchaseId());
            tempPurchaseEntity.get().setTotalAmount(tempPurchaseEntity.get().getTotalAmount()-tempPurchaseProductCartEntity.get().getNetAmount());
            tempPurchaseRepo.save(tempPurchaseEntity.get());
            tempPurchaseProductCartRepo.deleteById(proCartId);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Record is deleted successfully!");
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't delete the record!"));
        }
        return response;
    }

    @Override
    public NonPaginatedResponse getAllTempPurchaseCartItems(Long purchaseId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<TempPurchaseProductCartEntity> purchaseCartItems = tempPurchaseProductCartRepo.findAll();

            List<TempPurchaseProductCartDto> anItemDto = purchaseCartItems.stream()
                    .map(TempPurchaseProductCartDto::new)
                    .toList();

            response.setResult(anItemDto);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Purchase Cart records Retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve cart records!"));
        }
        return response;
    }

    @Override
    public NonPaginatedResponse selectTempPurchaseCartRecords(Long purchaseId, String exitingChar) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<TempPurchaseEntity> selectedPurchase = tempPurchaseRepo.findById(purchaseId);
            List<TempPurchaseProductCartEntity> existingCartDetails = tempPurchaseProductCartRepo.findByStockEntity_ItemNameContaining(exitingChar);
            if (existingCartDetails.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Couldn't find item!"));
                return response;
            }
            List<TempPurchaseProductCartDto> cartItemsDto = existingCartDetails.stream()
                    .map(TempPurchaseProductCartDto::new)
                    .toList();
            response.setStatus(HttpStatus.ACCEPTED);
            response.setErrors(List.of("corresponding details to the given characters found!"));
            response.setResult(cartItemsDto);

        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't find item!"));
        }
        return response;
    }

    @Override
    public NonPaginatedResponse updateTempPurchaseCartRecord(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<TempPurchaseEntity> selectedTempPurchaseInvoice = tempPurchaseRepo.findById(tempPurchaseProductCartDto.getTempPurchaseEntity().getPurchaseId());
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

            Optional<TempPurchaseProductCartEntity> selectedPurchaseCartRec = tempPurchaseProductCartRepo.findById(tempPurchaseProductCartDto.getProductCartId());
            if (selectedPurchaseCartRec.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Record isn't exist!"));
                return response;
            }
            updateCartRecords(tempPurchaseProductCartDto, selectedPurchaseCartRec, selectedStockItem);
            updatePurchaseInvoiceNetAmount(tempPurchaseProductCartDto, selectedTempPurchaseInvoice, selectedStockItem);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Successfully updated the selected item details!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't update cart record!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private void updatePurchaseInvoiceNetAmount(TempPurchaseProductCartDto tempPurchaseProductCartDto, Optional<TempPurchaseEntity> selectedTempPurchaseInvoice, Optional<StockEntity> selectedStockItem) {
        double total = selectedStockItem.get().getSellingPrice() * tempPurchaseProductCartDto.getQuantity();
        double totalDiscount = tempPurchaseProductCartDto.getQuantity() * tempPurchaseProductCartDto.getDiscount();
        selectedTempPurchaseInvoice.get().setTotalAmount(total - totalDiscount);
        tempPurchaseRepo.save(selectedTempPurchaseInvoice.get());
    }

    private void updateCartRecords(TempPurchaseProductCartDto tempPurchaseProductCartDto, Optional<TempPurchaseProductCartEntity> selectedPurchaseCartRec,Optional<StockEntity> selectedStockItem) {

        TempPurchaseProductCartEntity selectedProductCartRecDetails = selectedPurchaseCartRec.get();
        selectedProductCartRecDetails.setDiscount(tempPurchaseProductCartDto.getDiscount());
        selectedProductCartRecDetails.setQuantity(tempPurchaseProductCartDto.getQuantity());
        selectedProductCartRecDetails.setGrossAmount(tempPurchaseProductCartDto.getQuantity()*selectedStockItem.get().getSellingPrice());
        double totalDiscount = tempPurchaseProductCartDto.getDiscount()*tempPurchaseProductCartDto.getQuantity();
        double totalNetAmount = selectedProductCartRecDetails.getGrossAmount() - totalDiscount;
        selectedProductCartRecDetails.setNetAmount(totalNetAmount);
        tempPurchaseProductCartRepo.save(selectedProductCartRecDetails);
    }
}
