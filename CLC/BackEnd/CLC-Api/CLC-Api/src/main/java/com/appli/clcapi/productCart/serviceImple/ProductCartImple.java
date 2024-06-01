package com.appli.clcapi.productCart.serviceImple;


import com.appli.clcapi.common.constants.ProductCartConstants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.productCart.dto.ProductCartDto;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.productCart.repository.ProductCartRepo;
import com.appli.clcapi.productCart.service.ProductCartService;
import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.stock.repository.StockRepo;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;

import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductCartImple implements ProductCartService {
    private final ProductCartRepo productCartRepo;
    private final StockRepo stockRepo;
    private final TempInvoiceRepo tempInvoiceRepo;
    private static final Logger logger = LoggerFactory.getLogger(ProductCartImple.class);
    @Override
    @Transactional
    public NonPaginatedResponse addToCart(ProductCartDto productCartDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            Long stockId = productCartDto.getStockDto().getStockId();
            Long tempInvoiceId = productCartDto.getTempInvoiceDto().getTempInvoiceId();

            StockEntity stocksFromStockEntity = stockRepo.findById(stockId)
                    .orElseThrow(()-> new IllegalArgumentException("Stock with ID "+ stockId + " not found"));

            Optional<ProductCartEntity> itemInCart = productCartRepo.findByStockEntity_StockIdAndTempInvoiceEntity_TempInvoiceId(stockId,tempInvoiceId);
            double newQtyToStock = stocksFromStockEntity.getQuantity() - productCartDto.getQuantity();

            Optional<TempInvoiceEntity> tempInvoiceEntity = tempInvoiceRepo.findById(tempInvoiceId);

            if(itemInCart.isEmpty()) {
                ProductCartEntity returnedProduct = addNewItem(productCartDto);

                updateIntoStockEntity(stocksFromStockEntity, newQtyToStock);

                double currentNetAmountInTempInvoice = tempInvoiceEntity.get().getNetAmount();
                tempInvoiceEntity.get().setNetAmount(currentNetAmountInTempInvoice + productCartDto.getNetAmount());
                tempInvoiceRepo.save(tempInvoiceEntity.get());

                response.setSuccessMessage(ProductCartConstants.PRODUCT_HAS_BEEN_ADDED_INTO_THE_CART_SUCCESSFULLY);
                ProductCartDto aProductIntoTheCart = new ProductCartDto(returnedProduct);
                response.setResult(aProductIntoTheCart);
                response.setStatus(HttpStatus.CREATED);
            }else{
                ProductCartEntity itemInCartDetails = itemInCart.get();
                ProductCartEntity insertedProduct = addMoreQuantity(itemInCartDetails,productCartDto, stocksFromStockEntity, tempInvoiceEntity);

                updateIntoStockEntity(stocksFromStockEntity, newQtyToStock);


                response.setSuccessMessage(ProductCartConstants.MORE_QUANTITY_HAS_BEEN_UPDATED_TO_THE_PRODUCT);
                ProductCartDto aProductIntoTheCart = new ProductCartDto(insertedProduct);
                response.setResult(aProductIntoTheCart);
                response.setStatus(HttpStatus.ACCEPTED);
            }

        }catch (Exception e){
            logger.error("An error occurred while registering product cart", e);
            response.setSuccessMessage(null);
            response.setErrors(Arrays.asList("An error occurred while registering product cart"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private void updateIntoStockEntity(StockEntity stocksFromStockEntity, double newQtyToStock) {
        stocksFromStockEntity.setQuantity(newQtyToStock);
        stockRepo.save(stocksFromStockEntity);
        stockRepo.flush();
    }


    private ProductCartEntity addNewItem(ProductCartDto productCartDto) {

        StockDto stockDto = productCartDto.getStockDto();
        TempInvoiceDto tempInvoiceDto = productCartDto.getTempInvoiceDto();
        ProductCartEntity aProductIntoCart = ProductCartEntity.builder()
                .proCartId(productCartDto.getProCartId())
                .discount(productCartDto.getDiscount())
                .netAmount(productCartDto.getNetAmount())
                .quantity(productCartDto.getQuantity())
                .total(productCartDto.getTotal())
                .tempInvoiceEntity(new TempInvoiceEntity(tempInvoiceDto))
                .stockEntity(new StockEntity(stockDto)).build();
        return productCartRepo.save(aProductIntoCart);
    }

    private ProductCartEntity addMoreQuantity(ProductCartEntity existingItemsDetails, ProductCartDto productCartDto, StockEntity stocksFromStockEntity, Optional<TempInvoiceEntity> tempInvoiceEntity) {
        Double updatedQty = existingItemsDetails.getQuantity() + productCartDto.getQuantity();
        existingItemsDetails.setQuantity(updatedQty);
        Double discount = existingItemsDetails.getDiscount() + productCartDto.getDiscount();
        existingItemsDetails.setDiscount(discount);
        Double total = (stocksFromStockEntity.getSellingPrice() * updatedQty);
//        long total = existingItemsDetails.getTotal() + totalForNewAddition;
        existingItemsDetails.setTotal(total);
        Double netAmount = total - (updatedQty * discount);
        existingItemsDetails.setNetAmount(netAmount);

        tempInvoiceEntity.get().setNetAmount(netAmount);
        tempInvoiceRepo.save(tempInvoiceEntity.get());

        return productCartRepo.save(existingItemsDetails);


    }
    @Override
    @Transactional
    public NonPaginatedResponse delete(Long cartId){
        NonPaginatedResponse response = new NonPaginatedResponse();

        ProductCartEntity anItemInCart =productCartRepo.findById(cartId).orElseThrow();

        StockEntity stockEntity = anItemInCart.getStockEntity();
        Optional<StockEntity> aStock = stockRepo.findById(stockEntity.getStockId());



        Double noOfQtyToBeDeleted = anItemInCart.getQuantity();
        Double currentQtyInStock = aStock.get().getQuantity();
        aStock.get().setQuantity((currentQtyInStock + noOfQtyToBeDeleted));

        Optional<TempInvoiceEntity> tempInvoiceEntity = tempInvoiceRepo.findById(anItemInCart.getTempInvoiceEntity().getTempInvoiceId());
        tempInvoiceEntity.get().setNetAmount(tempInvoiceEntity.get().getNetAmount()- anItemInCart.getNetAmount());
        tempInvoiceRepo.save(tempInvoiceEntity.get());


        stockRepo.save(aStock.get());
        stockRepo.flush();

        productCartRepo.deleteById(cartId);
        productCartRepo.flush();

        ProductCartDto productCartDto = new ProductCartDto(anItemInCart);
        Long stockId = aStock.get().getStockId();
        String itemName = aStock.get().getItemName();

        response.setSuccessMessage("The Item :- "+ stockId +"-"+ itemName +" Successfully removed from the cart");
        response.setResult(productCartDto);
        response.setStatus(HttpStatus.OK);
        return response;
    }
    @Override
    public NonPaginatedResponse getAll(Long invoiceId){
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            List<ProductCartEntity> cartList = productCartRepo.findByTempInvoiceEntity_TempInvoiceId(invoiceId);
            List<ProductCartDto> anItemCartForView = new ArrayList<>();
            for (ProductCartEntity cartListFromEntity : cartList) {
                ProductCartDto productCartDto = new ProductCartDto(cartListFromEntity);
                anItemCartForView.add(productCartDto);
            }
            response.setResult(List.of(anItemCartForView));
            response.setStatus(HttpStatus.OK);
            response.setSuccessMessage("Data is retrieved");
        }
        catch (Exception e){
          response.setStatus(HttpStatus.BAD_REQUEST);
          response.setErrors(Arrays.asList("couldn't find any Data"));
        }
        return response;
    }



    @Override
        @Transactional
        public NonPaginatedResponse update(ProductCartDto productCartDto){
                NonPaginatedResponse response = new NonPaginatedResponse();

                Optional<ProductCartEntity> selectedCartRecord = productCartRepo.findById(productCartDto.getProCartId());
                double currentQtyInTheRecord = selectedCartRecord.get().getQuantity();
                double newlySelectedQty = productCartDto.getQuantity();
                ProductCartEntity cartRecordDetails = selectedCartRecord.get();

                Optional<TempInvoiceEntity> tempInvoiceEntity = tempInvoiceRepo.findById(productCartDto.getTempInvoiceDto().getTempInvoiceId());
                if(selectedCartRecord.isPresent()){
                    double quantityChange  = currentQtyInTheRecord - newlySelectedQty;//130-200=-70
//                    double quantity = currentQtyInTheRecord - quantityChange;//130-(-70)=>130+70=200
                    double previousQtyAmount = selectedCartRecord.get().getNetAmount();

                    cartRecordDetails.setQuantity(productCartDto.getQuantity());
                    cartRecordDetails.setDiscount(productCartDto.getDiscount());
                    cartRecordDetails.setTotal(productCartDto.getTotal());
                    cartRecordDetails.setNetAmount(productCartDto.getNetAmount());


                    double netAmount = tempInvoiceEntity.get().getNetAmount() - previousQtyAmount;
                    tempInvoiceEntity.get().setNetAmount(netAmount+ productCartDto.getNetAmount());

                    tempInvoiceRepo.save(tempInvoiceEntity.get());
                    productCartRepo.save(cartRecordDetails);
                    productCartRepo.flush();



                    Long stockId = productCartDto.getStockDto().getStockId();
                    Optional<StockEntity> theStockTobeUpdated = stockRepo.findById(stockId);
                    StockEntity stockEntity = theStockTobeUpdated.get();
                    updateIntoStockEntity(stockEntity, stockEntity.getQuantity() + quantityChange);

                    response.setResult(null);
                    response.setSuccessMessage("The selected Item has been Successfully updated");
                    response.setStatus(HttpStatus.OK);
                    return response;
                }else{
                    response.setResult(null);
                    response.setErrors(Arrays.asList("Unsuccessful"));
                    response.setStatus(HttpStatus.NOT_FOUND);
                    return response;
                }
    
        }
    @Override
    public NonPaginatedResponse select(Long invoiceId, String exitingChar){
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ProductCartEntity> existingCartDetails = productCartRepo.findByTempInvoiceEntity_TempInvoiceIdAndStockEntity_ItemNameContaining(invoiceId, exitingChar);
            List<ProductCartDto> productCartDtoForView = new ArrayList<>();
            for (ProductCartEntity aRecordOfCart : existingCartDetails) {
                ProductCartDto productCartDto = new ProductCartDto(aRecordOfCart);
                productCartDtoForView.add(productCartDto);
            }
            response.setResult(productCartDtoForView);
            response.setStatus(HttpStatus.OK);
            response.setSuccessMessage("Searched Item has been found");

        }catch (Exception e){
            response.setErrors(Arrays.asList(e.toString()));
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setResult(null);
        }
        return response;
    }


}