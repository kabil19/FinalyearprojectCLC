package com.appli.clcapi.invoiceReturn.serviceImpl;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.confirmCartItems.dto.ConfirmProductCartDto;
import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import com.appli.clcapi.confirmInvoice.confirmCartItems.repository.ConfirmProductCartRepo;
import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmSalesInvoiceRepo;
import com.appli.clcapi.invoiceReturn.dto.SalesInvoiceCartReturnDto;
import com.appli.clcapi.invoiceReturn.entity.SalesInvoiceCartReturnEntity;
import com.appli.clcapi.invoiceReturn.entity.SalesReturnInvoice;
import com.appli.clcapi.invoiceReturn.repository.SalesInvoiceCartReturnRepo;
import com.appli.clcapi.invoiceReturn.repository.SalesInvoiceReturnRepo;
import com.appli.clcapi.invoiceReturn.service.SalesInvoiceCartReturnService;
import com.appli.clcapi.stock.entity.StockEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SalesInvoiceCartReturnImpl implements SalesInvoiceCartReturnService {
    private final SalesInvoiceCartReturnRepo salesInvoiceCartReturnRepo;
    private final ConfirmProductCartRepo confirmProductCartRepo;
    private final SalesInvoiceReturnRepo salesInvoiceReturnRepo;
    private final ConfirmSalesInvoiceRepo confirmSalesInvoiceRepo;
    @Override
    @Transactional
    public NonPaginatedResponse addIntoSalesReturnCart(SalesInvoiceCartReturnDto salesInvoiceCartReturnDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            // Retrieve ConfirmSalesInvoiceEntity
            Optional<ConfirmSalesInvoiceEntity> confirmSalesInvoiceEntity =
                    confirmSalesInvoiceRepo.findById(salesInvoiceCartReturnDto.getSalesReturnInvoiceDto().getSalesReturnInvoiceId());

            double actualQtyInCart = 0.0;
            double actualDiscountGivenPerProduct = 0.0;

            // Check if the item exists in return cart or confirm cart
            Optional<SalesInvoiceCartReturnEntity> existingReturnCartItem =
                    salesInvoiceCartReturnRepo.findById(salesInvoiceCartReturnDto.getSalesRetProductCartId());
            if (existingReturnCartItem.isPresent()) {
                SalesInvoiceCartReturnEntity returnCartItem = existingReturnCartItem.get();
                actualQtyInCart = returnCartItem.getQuantity();
                actualDiscountGivenPerProduct = returnCartItem.getDiscount();
                salesInvoiceCartReturnRepo.deleteById(salesInvoiceCartReturnDto.getSalesRetProductCartId());
            } else {
                Optional<ConfirmProductCartEntity> confirmCartItem =
                        confirmProductCartRepo.findById(salesInvoiceCartReturnDto.getSalesRetProductCartId());
                if (confirmCartItem.isPresent()) {
                    ConfirmProductCartEntity productCart = confirmCartItem.get();
                    actualQtyInCart = productCart.getQuantity();
                    actualDiscountGivenPerProduct = productCart.getDiscount();
                }
            }
            if(salesInvoiceCartReturnDto.getQuantity()>actualQtyInCart){
                response.setErrors(List.of("The selected quantity exceeds the available stock!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }

            // Calculate return cart values
            double newQtyInReturnCart = actualQtyInCart - salesInvoiceCartReturnDto.getQuantity();
            double sellingPrice = salesInvoiceCartReturnDto.getStockDto().getSellingPrice();
            double remainingProductsValue = (sellingPrice - actualDiscountGivenPerProduct) * newQtyInReturnCart;
            double netValueIfReturned = (sellingPrice - actualDiscountGivenPerProduct) * salesInvoiceCartReturnDto.getQuantity();

            if (confirmSalesInvoiceEntity.isPresent()) {
                if (!handleReturnValidation(confirmSalesInvoiceEntity.get(), netValueIfReturned, response)) {
                    return response;
                }
            }

            // Create or update the return invoice
            createOrUpdateSalesReturnInvoice(confirmSalesInvoiceEntity, salesInvoiceCartReturnDto, netValueIfReturned);

            // Update the returnAmount in ConfirmSalesInvoiceEntity
            if (confirmSalesInvoiceEntity.isPresent()) {
                ConfirmSalesInvoiceEntity entity = confirmSalesInvoiceEntity.get();
                entity.setReturnAmount(entity.getReturnAmount() + netValueIfReturned);
                confirmSalesInvoiceRepo.save(entity);
            }

            // Save the return cart entity
            SalesInvoiceCartReturnEntity returnedEntity = buildSalesInvoiceCartReturnEntity(
                    salesInvoiceCartReturnDto, newQtyInReturnCart, sellingPrice, remainingProductsValue, actualDiscountGivenPerProduct
            );
            salesInvoiceCartReturnRepo.save(returnedEntity);

            response.setSuccessMessage("The selected Items successfully Returned!");
            response.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of(e.getMessage()));
        }
        return response;
    }

    // Helper Methods
    private boolean handleReturnValidation(ConfirmSalesInvoiceEntity confirmSalesInvoiceEntity, double netValueIfReturned, NonPaginatedResponse response) {
        double invoiceNetAmountAfterReturn = confirmSalesInvoiceEntity.getNetAmount() - netValueIfReturned;

        if (confirmSalesInvoiceEntity.getPaidAmount() > invoiceNetAmountAfterReturn) {
            double amountToBeReturned = confirmSalesInvoiceEntity.getPaidAmount() - invoiceNetAmountAfterReturn;
            response.setErrors(List.of("Return the pay of " +
                    (amountToBeReturned - confirmSalesInvoiceEntity.getMainDiscount()) + " to make this return!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return false;
        }
        return true;
    }

    private void createOrUpdateSalesReturnInvoice(Optional<ConfirmSalesInvoiceEntity> confirmSalesInvoiceEntity,
                                                  SalesInvoiceCartReturnDto salesInvoiceCartReturnDto,
                                                  double netValueIfReturned) {
        if (salesInvoiceReturnRepo.existsById(salesInvoiceCartReturnDto.getSalesReturnInvoiceDto().getSalesReturnInvoiceId())) {
            Optional<SalesReturnInvoice> existingInvoice =
                    salesInvoiceReturnRepo.findById(salesInvoiceCartReturnDto.getSalesReturnInvoiceDto().getSalesReturnInvoiceId());
            if (existingInvoice.isPresent()) {
                SalesReturnInvoice returnInvoice = existingInvoice.get();
                returnInvoice.setReturnAmount(returnInvoice.getReturnAmount() + netValueIfReturned);
                returnInvoice.setNetAmount(returnInvoice.getNetAmount() - netValueIfReturned);
                salesInvoiceReturnRepo.saveAndFlush(returnInvoice);
            }
        } else if (confirmSalesInvoiceEntity.isPresent()) {
            SalesReturnInvoice newReturnInvoice = buildSalesReturnInvoice(confirmSalesInvoiceEntity.get(), netValueIfReturned);
            salesInvoiceReturnRepo.saveAndFlush(newReturnInvoice);
        }
    }

    private SalesReturnInvoice buildSalesReturnInvoice(ConfirmSalesInvoiceEntity confirmSalesInvoiceEntity, double netValueIfReturned) {
        return SalesReturnInvoice.builder()
                .invoiceReference(confirmSalesInvoiceEntity.getInvoiceReference())
                .salesReturnInvoiceId(confirmSalesInvoiceEntity.getConfirmInvoiceId())
                .advancePayment(confirmSalesInvoiceEntity.getAdvancePayment())
                .date(confirmSalesInvoiceEntity.getDate())
                .mainDiscount(confirmSalesInvoiceEntity.getMainDiscount())
                .returnAmount(netValueIfReturned)
                .netAmount(confirmSalesInvoiceEntity.getNetAmount() - netValueIfReturned)
                .paidAmount(confirmSalesInvoiceEntity.getPaidAmount())
                .build();
    }

    private SalesInvoiceCartReturnEntity buildSalesInvoiceCartReturnEntity(SalesInvoiceCartReturnDto salesInvoiceCartReturnDto,
                                                                           double newQtyInReturnCart,
                                                                           double sellingPrice,
                                                                           double remainingProductsValue,
                                                                           double actualDiscountGivenPerProduct) {
        return SalesInvoiceCartReturnEntity.builder()
                .salesReturnInvoice(new SalesReturnInvoice(salesInvoiceCartReturnDto.getSalesReturnInvoiceDto()))
                .stockEntity(new StockEntity(salesInvoiceCartReturnDto.getStockDto()))
                .total(newQtyInReturnCart * sellingPrice)
                .quantity(newQtyInReturnCart)
                .netAmount(remainingProductsValue)
                .salesRetProductCartId(salesInvoiceCartReturnDto.getSalesRetProductCartId())
                .discount(actualDiscountGivenPerProduct)
                .build();
    }


   /* public NonPaginatedResponse addIntoSalesReturnCart(SalesInvoiceCartReturnDto salesInvoiceCartReturnDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            Optional<ConfirmSalesInvoiceEntity> confirmSalesInvoiceEntity = confirmSalesInvoiceRepo.findById
                    (salesInvoiceCartReturnDto.getSalesReturnInvoiceDto().getSalesReturnInvoiceId());
            double actualQtyInCart = 0.0;
            double actualDiscountGivenPerProduct = 0.0;
            if(salesInvoiceCartReturnRepo.existsById(salesInvoiceCartReturnDto.getSalesRetProductCartId())){
                Optional<SalesInvoiceCartReturnEntity> remainingProducts = salesInvoiceCartReturnRepo.findById(salesInvoiceCartReturnDto.getSalesRetProductCartId());
                if(remainingProducts.isPresent()){
                    actualQtyInCart = remainingProducts.get().getQuantity();//3
                    actualDiscountGivenPerProduct = remainingProducts.get().getDiscount();//12
                }
                salesInvoiceCartReturnRepo.deleteById(salesInvoiceCartReturnDto.getSalesRetProductCartId());
            }else{
                Optional<ConfirmProductCartEntity> selectedProductDetailsFromTheConfirmCart = confirmProductCartRepo
                        .findById(salesInvoiceCartReturnDto.getSalesRetProductCartId());

                if(selectedProductDetailsFromTheConfirmCart.isPresent()){
                    actualQtyInCart = selectedProductDetailsFromTheConfirmCart.get().getQuantity();
                    actualDiscountGivenPerProduct = selectedProductDetailsFromTheConfirmCart.get().getDiscount();
                }
            }
            double newQtyInReturnCart = actualQtyInCart - salesInvoiceCartReturnDto.getQuantity();
            double sellingPrice = salesInvoiceCartReturnDto.getStockDto().getSellingPrice();
            double remainingProductsValue = 0.0;
            double netValueIfReturned = 0.0;
            if(confirmSalesInvoiceEntity.isPresent()){
                remainingProductsValue = (sellingPrice - actualDiscountGivenPerProduct) * newQtyInReturnCart;
                netValueIfReturned = ((sellingPrice - actualDiscountGivenPerProduct ) * salesInvoiceCartReturnDto.getQuantity());
                double invoiceNetAmountIfTheReturnMade = confirmSalesInvoiceEntity.get().getNetAmount() - netValueIfReturned;
                if(confirmSalesInvoiceEntity.get().getPaidAmount() > (invoiceNetAmountIfTheReturnMade)){//
                    double theAmountToBeReturned = confirmSalesInvoiceEntity.get().getPaidAmount() - invoiceNetAmountIfTheReturnMade;
                    response.setErrors(List.of("Return the pay of "+(theAmountToBeReturned-confirmSalesInvoiceEntity.get().getMainDiscount()) + " to make this return!"));
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    return response;
                }
            }

            if(!salesInvoiceReturnRepo.existsById(salesInvoiceCartReturnDto.getSalesReturnInvoiceDto().getSalesReturnInvoiceId())){
                if(confirmSalesInvoiceEntity.isPresent()){
                    SalesReturnInvoice salesReturnInvoice = new SalesReturnInvoice();
                    salesReturnInvoice.setInvoiceReference(confirmSalesInvoiceEntity.get().getInvoiceReference());
                    salesReturnInvoice.setSalesReturnInvoiceId(confirmSalesInvoiceEntity.get().getConfirmInvoiceId());
                    salesReturnInvoice.setAdvancePayment(confirmSalesInvoiceEntity.get().getAdvancePayment());
                    salesReturnInvoice.setDate(confirmSalesInvoiceEntity.get().getDate());
                    salesReturnInvoice.setMainDiscount(confirmSalesInvoiceEntity.get().getMainDiscount());
                    salesReturnInvoice.setReturnAmount(netValueIfReturned);
                    salesReturnInvoice.setNetAmount((confirmSalesInvoiceEntity.get().getNetAmount())-netValueIfReturned);
                    salesReturnInvoice.setPaidAmount(confirmSalesInvoiceEntity.get().getPaidAmount());
                    salesInvoiceReturnRepo.saveAndFlush(salesReturnInvoice);
                }
            }else{
                if(confirmSalesInvoiceEntity.isPresent()) {
                    Optional<SalesReturnInvoice> existingReturnInvoice = salesInvoiceReturnRepo.
                            findById(salesInvoiceCartReturnDto.getSalesReturnInvoiceDto().getSalesReturnInvoiceId());
                    if(existingReturnInvoice.isPresent()){
                        existingReturnInvoice.get().setReturnAmount(existingReturnInvoice.get().getReturnAmount()+netValueIfReturned);
                        existingReturnInvoice.get().setNetAmount(existingReturnInvoice.get().getNetAmount()-netValueIfReturned);

                        salesInvoiceReturnRepo.saveAndFlush(existingReturnInvoice.get());
                    }
                }
            }

            SalesInvoiceCartReturnEntity returnedEntity = SalesInvoiceCartReturnEntity.builder()
                    .salesReturnInvoice(new SalesReturnInvoice(salesInvoiceCartReturnDto.getSalesReturnInvoiceDto()))
                    .stockEntity(new StockEntity(salesInvoiceCartReturnDto.getStockDto()))
                    .total(newQtyInReturnCart * sellingPrice)
                    .quantity(newQtyInReturnCart)
                    .netAmount(remainingProductsValue)
                    .salesRetProductCartId(salesInvoiceCartReturnDto.getSalesRetProductCartId())
                    .discount(actualDiscountGivenPerProduct)
                    .build();

            salesInvoiceCartReturnRepo.save(returnedEntity);
            response.setSuccessMessage("The selected Items successfully Returned!");
            response.setStatus(HttpStatus.OK);
            return response;
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of(e.getMessage()));
        }
        return null;
    }*/

    @Override
    public NonPaginatedResponse retrieveRemainingCartItems(long salesRetInvoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        // Retrieve entities from the repositories
        List<ConfirmProductCartEntity> confirmProductCartEntities =
                confirmProductCartRepo.findByConfirmSalesInvoiceEntity_ConfirmInvoiceId(salesRetInvoiceId);
        List<SalesInvoiceCartReturnEntity> salesInvoiceCartReturnEntities =
                salesInvoiceCartReturnRepo.findAllBySalesReturnInvoice_SalesReturnInvoiceId(salesRetInvoiceId);

        // Convert entities to DTOs
        List<SalesInvoiceCartReturnDto> returnCartDto =
                salesInvoiceCartReturnEntities.stream()
                        .map(SalesInvoiceCartReturnDto::new)
                        .toList();
        List<ConfirmProductCartDto> confirmCartDto =
                confirmProductCartEntities.stream()
                        .map(ConfirmProductCartDto::new)
                        .toList();

        // Extract salesRetProductCartId from returnCartDto for filtering
        Set<Long> salesRetProductCartIds = returnCartDto.stream()
                .map(SalesInvoiceCartReturnDto::getSalesRetProductCartId)
                .collect(Collectors.toSet());

        // Filter confirmCartDto to exclude matching confirmProductCartIds
        List<ConfirmProductCartDto> filteredConfirmCartDto = confirmCartDto.stream()
                .filter(dto -> !salesRetProductCartIds.contains(dto.getConfirmProductCartId()))
                .toList();

        // Combine both DTO lists
        List<Object> combinedCartDto = new ArrayList<>();
        combinedCartDto.addAll(returnCartDto);
        combinedCartDto.addAll(filteredConfirmCartDto);

        response.setResult(combinedCartDto);
        return response;

    }


}
