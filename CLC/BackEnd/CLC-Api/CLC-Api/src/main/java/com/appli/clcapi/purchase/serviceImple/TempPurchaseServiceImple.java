package com.appli.clcapi.purchase.serviceImple;

import com.appli.clcapi.common.constants.TempPurchaseConstants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.purchase.dto.TempPurchaseDto;
import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.purchase.repository.TempPurchaseRepo;
import com.appli.clcapi.purchase.service.TempPurchaseService;
import com.appli.clcapi.vendor.dto.VendorDto;
import com.appli.clcapi.vendor.entity.VendorEntity;
import com.appli.clcapi.vendor.repository.VendorRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TempPurchaseServiceImple implements TempPurchaseService {

    private final VendorRepo vendorRepo;
    private final TempPurchaseRepo tempPurchaseRepo;
    @Override
    public NonPaginatedResponse addToTempPurchase(TempPurchaseDto tempPurchaseDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            Optional<VendorEntity> vendorEntity = vendorRepo.findById(tempPurchaseDto.getVendorDto().getVendorId());
            List<TempPurchaseEntity> tempPurchaseEntity = tempPurchaseRepo.findAll();
            if(tempPurchaseEntity.isEmpty()){
                if (!vendorEntity.isEmpty()) {
                    TempPurchaseEntity aTempPurchase = TempPurchaseEntity.builder()
                            .purchasedDate(tempPurchaseDto.getPurchasedDate())
                            .purchaseInvoiceNO(tempPurchaseDto.getPurchaseInvoiceNO())
                            .vendorEntity(new VendorEntity(tempPurchaseDto.getVendorDto()))
                            .totalAmount(0.00)
                            .build();
                    response.setResult(tempPurchaseRepo.save(aTempPurchase));
                    response.setSuccessMessage(TempPurchaseConstants.PURCHASE_HAS_BEEN_CREATED);
                    response.setStatus(HttpStatus.ACCEPTED);
                }else{
                    response.setErrors(List.of("Vendor isn't exist!"));
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    return response;
                }
            }else{
                response.setErrors(List.of("Already a purchase invoice is in creation!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }


        }catch (Exception e){
            e.printStackTrace();
            response.setErrors(List.of("Purchase creation failed"));
            response.setStatus(HttpStatus.BAD_GATEWAY);
        }
        return response;
    }

    @Override
    public NonPaginatedResponse deleteTempPurchase(Long purchaseId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
             tempPurchaseRepo.deleteById(purchaseId);
             response.setStatus(HttpStatus.ACCEPTED);
             response.setSuccessMessage(TempPurchaseConstants.PURCHASE_HAS_BEEN_SUCCESSFULLY_DELETED);
        }catch (Exception e){
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_GATEWAY);
            response.setErrors(List.of("Couldn't delete the Purchase"));
        }
        return response;
    }

    public NonPaginatedResponse getAll(){
        NonPaginatedResponse response = new NonPaginatedResponse();
        TempPurchaseDto purchaseDto = new TempPurchaseDto();
        List<TempPurchaseEntity> tempPurchaseEntity = tempPurchaseRepo.findAll();
        response.setResult(tempPurchaseEntity.stream().map(
                aTempPurchase ->{
                    TempPurchaseDto tempPurchaseDto = new TempPurchaseDto();
                    tempPurchaseDto.setPurchaseId(aTempPurchase.getPurchaseId());
                    tempPurchaseDto.setPurchaseInvoiceNO(aTempPurchase.getPurchaseInvoiceNO());
                    tempPurchaseDto.setVendorDto(new VendorDto(aTempPurchase.getVendorEntity()));
                    return tempPurchaseDto;
                }
        ).toList());
        response.setStatus(HttpStatus.ACCEPTED);
        response.setSuccessMessage("TempPurchase is retrieved");
        return response;
    }
}
