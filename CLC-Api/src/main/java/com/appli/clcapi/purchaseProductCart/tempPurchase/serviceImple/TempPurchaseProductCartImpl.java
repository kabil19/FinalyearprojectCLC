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
public class TempPurchaseProductCartImpl implements TempPurchaseProductCartService {
    private final TempPurchaseProductCartRepo tempPurchaseProductCartRepo;
    private final TempPurchaseRepo tempPurchaseRepo;
    private final StockRepo stockRepo;

    @Override
    public NonPaginatedResponse addToTempPurchaseCart(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {

            Long purchaseId = tempPurchaseProductCartDto.getTempPurchaseEntity().getPurchaseId();
            Optional<TempPurchaseEntity> selectedTempPurchaseInvoice = tempPurchaseRepo.findById(purchaseId);
            Long stockId = tempPurchaseProductCartDto.getStockDto().getStockId();
            Optional<StockEntity> selectedStockItem = stockRepo.findById(stockId);
            if (selectedTempPurchaseInvoice.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Purchase Invoice isn't exist!"));
                return response;
            }
            if (selectedStockItem.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Stock isn't exist!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getQuantity() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Quantity can't be neither 0 nor less!"));
                return response;
            }
            if(tempPurchaseProductCartDto.getDiscount()<0){
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Discount can't be less than zero!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getSellingPrice() <= 0 && tempPurchaseProductCartDto.getPurchasePrice() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Prices can't be neither 0 nor less!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getSellingPrice() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Selling price can't be neither 0 nor less!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getPurchasePrice() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Purchase price can't be neither 0 nor less!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getPurchasePrice() > tempPurchaseProductCartDto.getSellingPrice()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Selling Price can't be lesser than Purchase Price!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getDiscount() >= tempPurchaseProductCartDto.getPurchasePrice()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Unit Discount can't be neither exceed nor equal the Purchase Price!"));
                return response;
            }


            Optional<TempPurchaseProductCartEntity> existingRecordFromTheCart = tempPurchaseProductCartRepo.
                    findByStockEntity_StockIdAndTempPurchaseEntity_PurchaseId(
                            stockId, purchaseId
                    );
            Optional<StockEntity> productFromTheStock = stockRepo.findById(stockId);
            if (existingRecordFromTheCart.isEmpty() && productFromTheStock.isPresent()) {
                createPurchaseCartRecord(tempPurchaseProductCartDto);
            } else
                existingRecordFromTheCart.ifPresent(tempPurchaseProductCartEntity -> addMoreItems(tempPurchaseProductCartDto, tempPurchaseProductCartEntity, selectedTempPurchaseInvoice));
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Product is successfully Added into the Cart!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Error Inserting products!"));
        }
        return response;
    }

    private void addMoreItems(
            TempPurchaseProductCartDto tempPurchaseProductCartDto,
            TempPurchaseProductCartEntity existingProductCartRecord,
            Optional<TempPurchaseEntity> selectedTempPurchaseInvoice) {
        if (selectedTempPurchaseInvoice.isPresent()){
            double newNetAmount = alterCartDetailsAndGetNewNetAmount(tempPurchaseProductCartDto, existingProductCartRecord);
            alterTempPurchase(selectedTempPurchaseInvoice.get(), newNetAmount);
        }

    }

    private double alterCartDetailsAndGetNewNetAmount(TempPurchaseProductCartDto tempPurchaseProductCartDto, TempPurchaseProductCartEntity existingProductCartRecord) {
        double existingQtyInCart = existingProductCartRecord.getQuantity();
        double newQty = tempPurchaseProductCartDto.getQuantity() + existingQtyInCart;

        double newDiscount = tempPurchaseProductCartDto.getDiscount() + existingProductCartRecord.getDiscount();

        double newGross = tempPurchaseProductCartDto.getPurchasePrice() * newQty;
        double newNetAmount = (newGross) - (newQty * newDiscount);

        existingProductCartRecord.setDiscount(newDiscount);
        existingProductCartRecord.setQuantity(newQty);
        existingProductCartRecord.setGrossAmount(newGross);
        existingProductCartRecord.setNetAmount(newNetAmount);
        tempPurchaseProductCartRepo.save(existingProductCartRecord);
        return newNetAmount;
    }

    private void alterTempPurchase(TempPurchaseEntity tempPurchaseEntity, double newNetAmount) {
        tempPurchaseEntity.setNetAmount(newNetAmount);
        tempPurchaseRepo.save(tempPurchaseEntity);
    }


    private void createPurchaseCartRecord(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        double quantity = tempPurchaseProductCartDto.getQuantity();
        double gross = (tempPurchaseProductCartDto.getPurchasePrice() * quantity);
        double netAmount = (gross - (quantity * tempPurchaseProductCartDto.getDiscount()));
        TempPurchaseProductCartEntity newPurchaseCartRecord = TempPurchaseProductCartEntity.builder()
                .productCartId(tempPurchaseProductCartDto.getProductCartId())
                .quantity(quantity)
                .purchasePrice(tempPurchaseProductCartDto.getPurchasePrice())
                .sellingPrice(tempPurchaseProductCartDto.getSellingPrice())
                .discount(tempPurchaseProductCartDto.getDiscount())
                .grossAmount(gross)
                .netAmount(netAmount)
                .stockEntity(new StockEntity(tempPurchaseProductCartDto.getStockDto()))
                .tempPurchaseEntity(new TempPurchaseEntity(tempPurchaseProductCartDto.getTempPurchaseEntity()))
                .build();
        tempPurchaseProductCartRepo.save(newPurchaseCartRecord);
        updateOnTempPurchase(tempPurchaseProductCartDto);
    }

    private void updateOnTempPurchase(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        Optional<TempPurchaseEntity> selectedPurchase = tempPurchaseRepo.findById(tempPurchaseProductCartDto.getTempPurchaseEntity().getPurchaseId());
        if (selectedPurchase.isPresent()) {
            selectedPurchase.get().setNetAmount(selectedPurchase.get().getNetAmount() + tempPurchaseProductCartDto.getNetAmount());
            tempPurchaseRepo.save(selectedPurchase.get());
        }
    }

    @Override
    public NonPaginatedResponse deleteTempPurchaseCartRecord(Long proCartId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<TempPurchaseProductCartEntity> tempPurchaseProductCartEntity = tempPurchaseProductCartRepo.findById(proCartId);
            if (tempPurchaseProductCartEntity.isPresent()) {
                Optional<TempPurchaseEntity> tempPurchaseEntity = tempPurchaseRepo.findById(tempPurchaseProductCartEntity.get().getTempPurchaseEntity().getPurchaseId());
                if (tempPurchaseEntity.isPresent()) {
                    tempPurchaseEntity.get().setNetAmount(tempPurchaseEntity.get().getNetAmount() - tempPurchaseProductCartEntity.get().getNetAmount());
                    tempPurchaseRepo.save(tempPurchaseEntity.get());
                    tempPurchaseProductCartRepo.deleteById(proCartId);
                    response.setStatus(HttpStatus.ACCEPTED);
                    response.setSuccessMessage("Record is deleted successfully!");
                }
            }
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
//            Optional<TempPurchaseEntity> selectedPurchase = tempPurchaseRepo.findById(purchaseId);
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
            Long purchaseId = tempPurchaseProductCartDto.getTempPurchaseEntity().getPurchaseId();
            Optional<TempPurchaseEntity> selectedTempPurchaseInvoice = tempPurchaseRepo.findById(purchaseId);
            Long stockId = tempPurchaseProductCartDto.getStockDto().getStockId();
            Optional<StockEntity> selectedStockItem = stockRepo.findById(stockId);
            if (selectedStockItem.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Stock isn't exist!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getQuantity() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Quantity can't be neither 0 nor less!"));
                return response;
            }
            if(tempPurchaseProductCartDto.getDiscount()<0){
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Discount can't be less than zero!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getSellingPrice() <= 0 && tempPurchaseProductCartDto.getPurchasePrice() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Prices can neither be 0 nor less!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getSellingPrice() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Selling price can neither be 0 nor less!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getPurchasePrice() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Purchase price can neither be 0 nor less!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getPurchasePrice() > tempPurchaseProductCartDto.getSellingPrice()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Selling Price can't be lesser than Purchase Price!"));
                return response;
            }
            if (tempPurchaseProductCartDto.getDiscount() >= tempPurchaseProductCartDto.getPurchasePrice()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("The unit discount can neither exceed nor equal the purchase price!"));
                return response;
            }
            if (selectedTempPurchaseInvoice.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Purchase Invoice isn't exist!"));
                return response;
            }
            Optional<TempPurchaseProductCartEntity> selectedPurchaseCartRec = tempPurchaseProductCartRepo.findById(tempPurchaseProductCartDto.getProductCartId());
            if (selectedPurchaseCartRec.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Record isn't exist!"));
                return response;
            }
            updateCartRecords(tempPurchaseProductCartDto, selectedPurchaseCartRec);
            updatePurchaseInvoiceNetAmount(tempPurchaseProductCartDto, selectedTempPurchaseInvoice);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Successfully updated the selected item details!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't update cart record!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private void updatePurchaseInvoiceNetAmount(TempPurchaseProductCartDto tempPurchaseProductCartDto, Optional<TempPurchaseEntity> selectedTempPurchaseInvoice) {
        if (selectedTempPurchaseInvoice.isPresent()) {
            double gross = tempPurchaseProductCartDto.getPurchasePrice() * tempPurchaseProductCartDto.getQuantity();
            double totalDiscount = tempPurchaseProductCartDto.getQuantity() * tempPurchaseProductCartDto.getDiscount();
            selectedTempPurchaseInvoice.get().setNetAmount(gross - totalDiscount);
            tempPurchaseRepo.save(selectedTempPurchaseInvoice.get());
        }
    }

    private void updateCartRecords(TempPurchaseProductCartDto tempPurchaseProductCartDto, Optional<TempPurchaseProductCartEntity> selectedPurchaseCartRec) {
        if (selectedPurchaseCartRec.isPresent()) {
            TempPurchaseProductCartEntity selectedProductCartRecDetails = selectedPurchaseCartRec.get();
            selectedProductCartRecDetails.setDiscount(tempPurchaseProductCartDto.getDiscount());
            selectedProductCartRecDetails.setQuantity(tempPurchaseProductCartDto.getQuantity());
            selectedProductCartRecDetails.setGrossAmount(tempPurchaseProductCartDto.getQuantity() * tempPurchaseProductCartDto.getPurchasePrice());
            double totalDiscount = tempPurchaseProductCartDto.getDiscount() * tempPurchaseProductCartDto.getQuantity();
            double totalGross = selectedProductCartRecDetails.getGrossAmount() - totalDiscount;
            selectedProductCartRecDetails.setNetAmount(totalGross);
            tempPurchaseProductCartRepo.save(selectedProductCartRecDetails);
        }
    }
}
