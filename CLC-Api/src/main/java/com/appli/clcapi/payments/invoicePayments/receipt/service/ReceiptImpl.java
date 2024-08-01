package com.appli.clcapi.payments.invoicePayments.receipt.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.invoicePayments.receipt.dto.ConfirmSalesInvoiceReceiptDto;
import com.appli.clcapi.payments.invoicePayments.receipt.entity.ConfirmSalesInvoiceReceiptEntity;
import com.appli.clcapi.payments.invoicePayments.receipt.repository.ConfirmSalesInvoiceReceiptRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ReceiptImpl implements ReceiptService {
    private final ConfirmSalesInvoiceReceiptRepo receiptRepo;

    @Override
    public NonPaginatedResponse getAllReceiptsOfTheInvoiceId(long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            List<ConfirmSalesInvoiceReceiptEntity> confirmSalesInvoiceReceiptEntities = receiptRepo.
                    findByConfirmInvoiceEntity_ConfirmInvoiceId(invoiceId);
            List<ConfirmSalesInvoiceReceiptDto> receiptDtos = confirmSalesInvoiceReceiptEntities.stream()
                    .map(ConfirmSalesInvoiceReceiptDto::new)
                    .toList();
            if(receiptDtos.isEmpty()){
                response.setErrors(List.of("No Receipt has been found for the selected Ref. Number!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            response.setResult(receiptDtos);
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
