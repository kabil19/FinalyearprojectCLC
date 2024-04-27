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
//    private final TempInvoiceRepo tempInvoiceRepo;
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
            long newQty = stocksFromStockEntity.getQuantity() - productCartDto.getQuantity();

            if(itemInCart.isEmpty()) {
                ProductCartEntity returnedProduct = addNewItem(productCartDto);
                stocksFromStockEntity.setQuantity(newQty);
                stockRepo.save(stocksFromStockEntity);
                response.setSuccessMessage(ProductCartConstants.PRODUCT_HAS_BEEN_ADDED_INTO_THE_CART_SUCCESSFULLY);
                ProductCartDto aProductIntoTheCart = new ProductCartDto(returnedProduct);
                response.setResult(aProductIntoTheCart);
                response.setStatus(HttpStatus.CREATED);
            }else{
                ProductCartEntity itemInCartDetails = itemInCart.get();
                ProductCartEntity returnedProduct = addMoreQuantity(itemInCartDetails,productCartDto);
                stocksFromStockEntity.setQuantity(newQty);
                stockRepo.save(stocksFromStockEntity);
                response.setSuccessMessage(ProductCartConstants.MORE_QUANTITY_HAS_BEEN_UPDATED_TO_THE_PRODUCT);
                ProductCartDto aProductIntoTheCart = new ProductCartDto(returnedProduct);
                response.setResult(aProductIntoTheCart);
                response.setStatus(HttpStatus.ACCEPTED);
            }
            return response;
        }catch (Exception e){
            logger.error("An error occurred while registering product cart", e);
            response.setSuccessMessage("");
            response.setErrors(Arrays.asList("An error occurred while registering product cart"));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }

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

    private ProductCartEntity addMoreQuantity(ProductCartEntity existingItemsDetails, ProductCartDto productCartDto) {
        existingItemsDetails.setQuantity(existingItemsDetails.getQuantity() + productCartDto.getQuantity());
        existingItemsDetails.setDiscount(existingItemsDetails.getDiscount() + productCartDto.getDiscount());
//        existingItemsDetails.setNetAmount(existingItemsDetails.getNetAmount() + productCartDto.getNetAmount());
        existingItemsDetails.setNetAmount(existingItemsDetails.getNetAmount() + productCartDto.getNetAmount());
        existingItemsDetails.setTotal(existingItemsDetails.getTotal() + productCartDto.getTotal());
        return productCartRepo.save(existingItemsDetails);
    }

    @Transactional
    public NonPaginatedResponse delete(Long cartId){
        NonPaginatedResponse response = new NonPaginatedResponse();
        ProductCartEntity anItemInCart =productCartRepo.findById(cartId).orElseThrow();
        Optional<StockEntity> aStock = stockRepo.findById(anItemInCart.getStockEntity().getStockId());
        Long selectedQty = anItemInCart.getQuantity();
        aStock.get().setQuantity((aStock.get().getQuantity()+selectedQty));
        stockRepo.save(aStock.get());
        productCartRepo.deleteById(cartId);
        productCartRepo.flush();
        ProductCartDto productCartDto = new ProductCartDto(anItemInCart);
        response.setSuccessMessage("The Item with the StockId:- "+ aStock.get().getStockId() +" Successfully removed from the cart");
        response.setResult(productCartDto);
        response.setStatus(HttpStatus.OK);
        return response;
    }

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
            response.setSuccessMessage("Data has been retrieved");
        }
        catch (Exception e){
          response.setStatus(HttpStatus.BAD_REQUEST);
          response.setErrors(Arrays.asList("couldn't find any Data"));

        }
        return response;
    }



}