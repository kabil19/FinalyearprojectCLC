package com.appli.clcapi.confirmInvoice.confirmCartItems.serviceImple;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.confirmCartItems.dto.ConfirmProductCartDto;

import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import com.appli.clcapi.confirmInvoice.confirmCartItems.repository.ConfirmProductCartRepo;
import com.appli.clcapi.confirmInvoice.confirmCartItems.service.ConfirmProductCartService;

import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.productCart.repository.ProductCartRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@RequiredArgsConstructor
@Service
public class ConfirmProductCartServiceImple implements ConfirmProductCartService {
    private final ProductCartRepo productCartRepo;
    private final ConfirmProductCartRepo confirmProductCartRepo;

    @Override
    @Transactional
    public Boolean confirmTheCartItems(Long invoiceId, ConfirmSalesInvoiceEntity confirmInvoice) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ProductCartEntity> listOfProCarts = productCartRepo.findByTempInvoiceEntity_TempInvoiceId(invoiceId);

            for (ProductCartEntity aCart : listOfProCarts) {
                ConfirmProductCartEntity confirmProductCartEntity = ConfirmProductCartEntity.builder()
                        .confirmProductCartId(aCart.getProCartId())
                        .discount(aCart.getDiscount())
                        .netAmount(aCart.getNetAmount())
                        .quantity(aCart.getQuantity())
                        .total(aCart.getTotal())
                        .confirmSalesInvoiceEntity(confirmInvoice)
                        .stockEntity(aCart.getStockEntity()).build();
                confirmProductCartRepo.save(confirmProductCartEntity);
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't Confirm the Cart Items"));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return false;
        }

    }

    @Override
    public NonPaginatedResponse getAllConfirmedProCartItemsByInvoiceId(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmProductCartEntity> cartList = confirmProductCartRepo.findByConfirmInvoiceEntity_ConfirmInvoiceId(invoiceId);
            List<ConfirmProductCartDto> confirmProductCartDtoList = cartList.stream()
                    .map(ConfirmProductCartDto::new)
                            .toList();


            response.setResult(confirmProductCartDtoList);
            response.setStatus(HttpStatus.OK);
            response.setSuccessMessage("Confirmed Sales invoice cart records Retrieved!");
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't retrieve Sales invoice cart records!"));
        }
        return response;
    }

}
