package com.appli.clcapi.confirmInvoice.confirmCartItems.serviceImple;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmInvoiceRepo;
import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import com.appli.clcapi.confirmInvoice.confirmCartItems.repository.ConfirmProductCartRepo;
import com.appli.clcapi.confirmInvoice.confirmCartItems.service.ConfirmProductCartService;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.productCart.repository.ProductCartRepo;
import com.appli.clcapi.stock.dto.StockDto;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ConfirmProductCartServiceImple implements ConfirmProductCartService {
    private final ProductCartRepo productCartRepo;
    private final ConfirmProductCartRepo confirmProductCartRepo;
    private final ConfirmInvoiceRepo confirmInvoiceRepo;

    @Override
    @Transactional
    public Boolean confirmTheCartItems(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            List<ProductCartEntity> listOfProCarts = productCartRepo.findByTempInvoiceEntity_TempInvoiceId(invoiceId);
            ConfirmInvoiceEntity confirmInvoiceEntity = confirmInvoiceRepo.findById(invoiceId).get();
            ConfirmInvoiceDto confirmInvoiceDto = new ConfirmInvoiceDto(confirmInvoiceEntity);
            for(ProductCartEntity aCart: listOfProCarts){
                StockDto aStockDto = new StockDto(aCart.getStockEntity());

                ConfirmProductCartEntity confirmProductCartEntity = ConfirmProductCartEntity.builder()
                        .confirmProductCartId(aCart.getProCartId())
                        .discount(aCart.getDiscount())
                        .netAmount(aCart.getNetAmount())
                        .quantity(aCart.getQuantity())
                        .total(aCart.getTotal())
                        .confirmInvoiceEntity(new ConfirmInvoiceEntity(confirmInvoiceDto))
                        .stockEntity(new StockEntity(aStockDto)).build();
                confirmProductCartRepo.save(confirmProductCartEntity);
            }
            return true;

        }catch (Exception e){
            e.printStackTrace();
            response.setErrors(Arrays.asList("Couldn't Confirm the Cart Items"));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return false;
        }

    }


}
