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
import java.util.List;
import java.util.Optional;

import static java.util.Objects.isNull;

@RequiredArgsConstructor
@Service
public class ProductCartImpl implements ProductCartService {
    private final ProductCartRepo productCartRepo;
    private final StockRepo stockRepo;
    private final TempInvoiceRepo tempInvoiceRepo;
    private static final Logger logger = LoggerFactory.getLogger(ProductCartImpl.class);

    private boolean isStockValid(long stockId) {
       return stockRepo.findById(stockId).isEmpty();
    }

    @Override
    @Transactional
    public NonPaginatedResponse addProductsToCart(ProductCartDto productCartDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            if (productCartDto.getQuantity() <= 0.0) {
                response.setErrors(List.of("The quantity can't be zero nor less!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            if (productCartDto.getDiscount() < 0) {
                response.setErrors(List.of("The discount can't be lesser than zero!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            if (isNull(productCartDto.getStockDto().getStockId()) || isStockValid(productCartDto.getStockDto().getStockId())) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Stock is not selected!"));
                return response;
            }
//          this is the instance that shows a unit discount is acquired 100% from the selling price
            if (productCartDto.getNetAmount() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("The unit discount can neither exceed nor equal the selling price!"));
                return response;
            }

            Long stockId = productCartDto.getStockDto().getStockId();
            Long tempInvoiceId = productCartDto.getTempInvoiceDto().getTempInvoiceId();
            Optional<StockEntity> stocksFromStockEntity = stockRepo.findById(stockId);
            if (stocksFromStockEntity.isEmpty()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Select an existing stock from the list!"));
                return response;
            }

            Optional<ProductCartEntity> itemInCart = productCartRepo.findByStockEntity_StockIdAndTempInvoiceEntity_TempInvoiceId(stockId, tempInvoiceId);
            double newQtyToStock = stocksFromStockEntity.get().getQuantity() - productCartDto.getQuantity();

            Optional<TempInvoiceEntity> tempInvoiceEntity = tempInvoiceRepo.findById(tempInvoiceId);
            if (tempInvoiceEntity.isEmpty()) {
                response.setErrors(List.of("The Temp sales invoice isn't exist!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            if (itemInCart.isEmpty()) {
                ProductCartEntity returnedProduct = addNewItem(productCartDto);

                updateIntoStockEntity(stocksFromStockEntity.get(), newQtyToStock);

                double currentNetAmountInTempInvoice = tempInvoiceEntity.get().getNetAmount();
                tempInvoiceEntity.get().setNetAmount(currentNetAmountInTempInvoice + productCartDto.getNetAmount());
                tempInvoiceRepo.save(tempInvoiceEntity.get());

                response.setSuccessMessage(ProductCartConstants.PRODUCT_HAS_BEEN_ADDED_INTO_THE_CART_SUCCESSFULLY);
                assert returnedProduct != null;
                ProductCartDto aProductIntoTheCart = new ProductCartDto(returnedProduct);
                response.setResult(aProductIntoTheCart);
                response.setStatus(HttpStatus.CREATED);
            } else {
                ProductCartEntity itemInCartDetails = itemInCart.get();
                ProductCartEntity insertedProduct = addMoreQuantity(itemInCartDetails, productCartDto, stocksFromStockEntity.get(), tempInvoiceEntity);

                updateIntoStockEntity(stocksFromStockEntity.get(), newQtyToStock);


                response.setSuccessMessage(ProductCartConstants.MORE_QUANTITY_HAS_BEEN_UPDATED_TO_THE_PRODUCT);
                assert insertedProduct != null;
                ProductCartDto aProductIntoTheCart = new ProductCartDto(insertedProduct);
                response.setResult(aProductIntoTheCart);
                response.setStatus(HttpStatus.ACCEPTED);
            }

        } catch (Exception e) {
            logger.error("An error occurred while registering product cart", e);
            response.setSuccessMessage(null);
            response.setErrors(List.of("An error occurred while registering product cart"));
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

        Optional<StockEntity> selectedStock = stockRepo.findById(productCartDto.getStockDto().getStockId());
        if (selectedStock.isPresent()) {
            StockDto stockDto = new StockDto(selectedStock.get());
            TempInvoiceDto tempInvoiceDto = productCartDto.getTempInvoiceDto();
            double grossAmount = productCartDto.getQuantity() * stockDto.getSellingPrice();
            double totalDiscount = productCartDto.getQuantity() * productCartDto.getDiscount();
            ProductCartEntity aProductIntoCart = ProductCartEntity.builder()
                    .proCartId(productCartDto.getProCartId())
                    .discount(productCartDto.getDiscount())
//                .netAmount(productCartDto.getNetAmount())
                    .netAmount(grossAmount - totalDiscount)
                    .quantity(productCartDto.getQuantity())
//                .total(productCartDto.getTotal())
                    .total(grossAmount)
                    .tempInvoiceEntity(new TempInvoiceEntity(tempInvoiceDto))
                    .stockEntity(new StockEntity(stockDto)).build();
            return productCartRepo.save(aProductIntoCart);
        }
        return null;
    }

    private ProductCartEntity addMoreQuantity(ProductCartEntity existingItemsDetails,
                                              ProductCartDto productCartDto,
                                              StockEntity stocksFromStockEntity,
                                              Optional<TempInvoiceEntity> tempInvoiceEntity) {
        if (tempInvoiceEntity.isPresent()) {
            Double updatedQty = existingItemsDetails.getQuantity() + productCartDto.getQuantity();
            existingItemsDetails.setQuantity(updatedQty);
            Double discount = existingItemsDetails.getDiscount() + productCartDto.getDiscount();
            existingItemsDetails.setDiscount(discount);
            double total = (stocksFromStockEntity.getSellingPrice() * updatedQty);
//        long total = existingItemsDetails.getTotal() + totalForNewAddition;
            existingItemsDetails.setTotal(total);
            Double netAmount = total - (updatedQty * discount);
            existingItemsDetails.setNetAmount(netAmount);

            tempInvoiceEntity.get().setNetAmount(netAmount);
            tempInvoiceRepo.save(tempInvoiceEntity.get());

            return productCartRepo.save(existingItemsDetails);

        }

        return null;
    }

    @Override
    @Transactional
    public NonPaginatedResponse deleteProductFromTheCart(Long cartId) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {
            ProductCartEntity anItemInCart = productCartRepo.findById(cartId).orElseThrow();

            StockEntity stockEntity = anItemInCart.getStockEntity();
            Optional<StockEntity> aStock = stockRepo.findById(stockEntity.getStockId());


            if (aStock.isPresent()) {
                Double noOfQtyToBeDeleted = anItemInCart.getQuantity();
                Double currentQtyInStock = aStock.get().getQuantity();
                aStock.get().setQuantity((currentQtyInStock + noOfQtyToBeDeleted));

                Optional<TempInvoiceEntity> tempInvoiceEntity = tempInvoiceRepo.findById(anItemInCart.getTempInvoiceEntity().getTempInvoiceId());
                if (tempInvoiceEntity.isPresent()) {
                    tempInvoiceEntity.get().setNetAmount(tempInvoiceEntity.get().getNetAmount() - anItemInCart.getNetAmount());
                    tempInvoiceRepo.save(tempInvoiceEntity.get());


                    stockRepo.save(aStock.get());
                    stockRepo.flush();

                    productCartRepo.deleteById(cartId);
                    productCartRepo.flush();

                    ProductCartDto productCartDto = new ProductCartDto(anItemInCart);

                    response.setSuccessMessage("The product is successfully deleted from the cart!");
                    response.setResult(productCartDto);
                    response.setStatus(HttpStatus.OK);

                } else {
                    response.setErrors(List.of("Invoice isn't present!"));
                    response.setStatus(HttpStatus.BAD_REQUEST);
                }
            } else {
                response.setErrors(List.of("Stock isn't present!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
            }
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't delete the product!"));
        }
        return response;
    }

    @Override
    public NonPaginatedResponse getAllTempProCartItemsByInvoiceId(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ProductCartEntity> cartList = productCartRepo.findByTempInvoiceEntity_TempInvoiceId(invoiceId);
//            List<ProductCartDto> anItemCartForView = new ArrayList<>();
//            for (ProductCartEntity cartListFromEntity : cartList) {
//                ProductCartDto productCartDto = new ProductCartDto(cartListFromEntity);
//                anItemCartForView.add(productCartDto);
//            }
            List<ProductCartDto> anItemCartForView = cartList.stream()
                    .map(ProductCartDto::new)
                    .toList();
            response.setResult(List.of(anItemCartForView));
            response.setStatus(HttpStatus.OK);
            response.setSuccessMessage("Temp Sales Invoice Cart Data are retrieved");
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("couldn't find any Temp Sales Invoice Data"));
        }
        return response;
    }

    @Override
    @Transactional
    public NonPaginatedResponse addMainDiscount(Long invoiceId, Double mainDiscount) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        TempInvoiceEntity aTempInvoice = tempInvoiceRepo.findById(invoiceId).orElseThrow(()->new RuntimeException("No such invoice exists!"));
        Double invoiceTotal = aTempInvoice.getNetAmount();
        if(invoiceTotal <= mainDiscount){
            response.setErrors(List.of("Add a valid discount amount!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }
        if(mainDiscount == 0){
            List<ProductCartEntity> cartList = productCartRepo.findByTempInvoiceEntity_TempInvoiceId(invoiceId);
            double totalNetAmount = cartList.stream()
                    .mapToDouble(ProductCartEntity::getNetAmount)
                    .sum();
            aTempInvoice.setMainDiscount(mainDiscount);
            aTempInvoice.setNetAmount(totalNetAmount);

        }else {
            aTempInvoice.setMainDiscount(mainDiscount);
            aTempInvoice.setNetAmount(aTempInvoice.getNetAmount() - mainDiscount);
        }
        tempInvoiceRepo.save(aTempInvoice);
        response.setStatus(HttpStatus.OK);
        response.setSuccessMessage("Discount Added!");
        return response;
    }
    @Override
    @Transactional
    public NonPaginatedResponse update(ProductCartDto productCartDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {
            if (productCartDto.getQuantity() <= 0.0) {
                response.setErrors(List.of("The quantity can't be zero nor less!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            if (productCartDto.getDiscount() < 0) {
                response.setErrors(List.of("The discount can't be lesser than zero!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            if (isNull(productCartDto.getStockDto().getStockId()) || isStockValid(productCartDto.getStockDto().getStockId())) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Stock is not selected!"));
                return response;
            }
//          this is the instance that unit discount is 100% from the selling price
            if (productCartDto.getNetAmount() <= 0) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("The unit discount can neither exceed nor equal the selling price!"));
                return response;
            }

            Optional<ProductCartEntity> selectedCartRecord = productCartRepo.findById(productCartDto.getProCartId());
            if (selectedCartRecord.isEmpty()) {
                response.setErrors(List.of("The Product isn't exist!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            double currentQtyInTheRecord = selectedCartRecord.get().getQuantity();
            double newlySelectedQty = productCartDto.getQuantity();
            ProductCartEntity cartRecordDetails = selectedCartRecord.get();

            Optional<TempInvoiceEntity> tempInvoiceEntity = tempInvoiceRepo.findById(productCartDto.getTempInvoiceDto().getTempInvoiceId());
            if (tempInvoiceEntity.isEmpty()) {
                response.setErrors(List.of("No such invoice exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            double quantityChange = currentQtyInTheRecord - newlySelectedQty;//130-200=-70
//                    double quantity = currentQtyInTheRecord - quantityChange;//130-(-70)=>130+70=200
            double previousQtyAmount = selectedCartRecord.get().getNetAmount();

            cartRecordDetails.setQuantity(productCartDto.getQuantity());
            cartRecordDetails.setDiscount(productCartDto.getDiscount());
            cartRecordDetails.setTotal(productCartDto.getTotal());
            cartRecordDetails.setNetAmount(productCartDto.getNetAmount());


            double netAmount = tempInvoiceEntity.get().getNetAmount() - previousQtyAmount;
            tempInvoiceEntity.get().setNetAmount(netAmount + productCartDto.getNetAmount());

            tempInvoiceRepo.save(tempInvoiceEntity.get());
            productCartRepo.save(cartRecordDetails);
            productCartRepo.flush();

            Long stockId = productCartDto.getStockDto().getStockId();
            Optional<StockEntity> theStockTobeUpdated = stockRepo.findById(stockId);
            if (theStockTobeUpdated.isEmpty()) {
                response.setErrors(List.of("No such Product exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            StockEntity stockEntity = theStockTobeUpdated.get();
            updateIntoStockEntity(stockEntity, stockEntity.getQuantity() + quantityChange);

            response.setResult(null);
            response.setSuccessMessage("The selected Item is Successfully updated!");
            response.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Error updating the cart!"));
        }

        return response;

    }

    @Override
    public NonPaginatedResponse select(Long invoiceId, String exitingChar) {
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
            response.setSuccessMessage("Searched Items are found!");

        } catch (Exception e) {
            response.setErrors(List.of(e.toString()));
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setResult(null);
        }
        return response;
    }


}