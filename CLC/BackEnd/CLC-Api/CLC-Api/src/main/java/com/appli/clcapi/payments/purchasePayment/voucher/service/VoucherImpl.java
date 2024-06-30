package com.appli.clcapi.payments.purchasePayment.voucher.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.purchasePayment.voucher.dto.VoucherDto;
import com.appli.clcapi.payments.purchasePayment.voucher.entity.VoucherEntity;
import com.appli.clcapi.payments.purchasePayment.voucher.repository.VoucherRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class VoucherImpl implements VoucherService {
    private final VoucherRepo voucherRepo;
    @Override
    public NonPaginatedResponse getAllVoucherOfThePurchaseId(long purchaseId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            List<VoucherEntity> voucherEntities = voucherRepo.findByConfirmPurchaseEntity_ConfirmPurchaseId(purchaseId);
            List<VoucherDto> voucherDto = voucherEntities.stream()
                    .map(VoucherDto::new)
                    .toList();
            if(voucherDto.isEmpty()){
                response.setErrors(List.of("No Voucher has been found for the selected Ref. Number!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            response.setResult(voucherDto);
            response.setSuccessMessage("Receipts Retrieved successfully!");
            response.setStatus(HttpStatus.ACCEPTED);
            return response;
        }catch (Exception e){
            e.printStackTrace();
            response.setErrors(List.of("Receipts Retrieved failed!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
}
