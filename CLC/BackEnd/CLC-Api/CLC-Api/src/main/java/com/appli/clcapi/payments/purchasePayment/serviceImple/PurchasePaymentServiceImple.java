package com.appli.clcapi.payments.purchasePayment.serviceImple;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayCardEntity;
import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayCashEntity;
import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayChequeEntity;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayCardRepo;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayCashRepo;
import com.appli.clcapi.paymentMethod.purchasePayMethods.repository.PurchasePayChequeRepo;
import com.appli.clcapi.payments.purchasePayment.dto.PurchasePaymentDto;
import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import com.appli.clcapi.payments.purchasePayment.repository.PurchasePaymentRepo;
import com.appli.clcapi.payments.purchasePayment.service.PurchasePaymentService;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import com.appli.clcapi.purchase.repository.ConfirmPurchaseRepo;
import com.appli.clcapi.vendor.entity.VendorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PurchasePaymentServiceImple implements PurchasePaymentService {

    private final PurchasePaymentRepo purchasePaymentRepo;
    private final PurchasePayCardRepo purchasePayCardRepo;
    private final PurchasePayCashRepo purchasePayCashRepo;
    private final PurchasePayChequeRepo purchasePayChequeRepo;
    private final ConfirmPurchaseRepo confirmPurchaseRepo;

    @Override
    public NonPaginatedResponse addToPurchaseInvoicePayment(PurchasePaymentDto purchasePaymentDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        Date now = new Date();
        try{

            PurchasePaymentEntity aPayment = PurchasePaymentEntity.builder()
                    .paymentType(purchasePaymentDto.getPaymentType())
                    .paidDate(now)
                    .paidAmount(purchasePaymentDto.getPaidAmount())
                    .confirmPurchaseEntity(new ConfirmPurchaseEntity(purchasePaymentDto.getConfirmPurchaseDto()))
                    .vendorEntity(new VendorEntity(purchasePaymentDto.getVendorDto()))
                    .build();
            PurchasePaymentEntity savedPaymentEntity =  purchasePaymentRepo.save(aPayment);

            addDetailsToTheRelevantPayMethod(purchasePaymentDto, savedPaymentEntity);
            if(isFieldsOnConfirmPurchaseUpdated(purchasePaymentDto)){
               response.setResult(purchasePaymentDto);
               response.setSuccessMessage("Payment has been made for the purchase invoice:- "+purchasePaymentDto.getConfirmPurchaseDto().getPurchaseInvoice());
               response.setStatus(HttpStatus.ACCEPTED);
               return response;
            }
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't update the payment to the invoice"));
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't add the payment details"));
       }
        return response;
    }

    private boolean isFieldsOnConfirmPurchaseUpdated(PurchasePaymentDto purchasePaymentDto) {
        Optional<ConfirmPurchaseEntity> confirmPurchaseEntity =  confirmPurchaseRepo.findById(
                purchasePaymentDto.getConfirmPurchaseDto().getConfirmPurchaseId()
        );
        if (confirmPurchaseEntity.isPresent()){
            double totalPaidAmount = confirmPurchaseEntity.get().getPaidAmount() + purchasePaymentDto.getPaidAmount();
            if(totalPaidAmount == confirmPurchaseEntity.get().getNetAmount()){
                confirmPurchaseEntity.get().setIsComplete(true);
                confirmPurchaseEntity.get().setPaidAmount(totalPaidAmount);
                confirmPurchaseRepo.save(confirmPurchaseEntity.get());
            }
            return true;
        }
       return false;
    }

    private void addDetailsToTheRelevantPayMethod(PurchasePaymentDto purchasePaymentDto, PurchasePaymentEntity savedPayment) {
        Date now = new Date();
        if(purchasePaymentDto.getPaymentType().equalsIgnoreCase("card")){
            PurchasePayCardEntity aCardPayment = PurchasePayCardEntity.builder()
                    .cardRefNo(purchasePaymentDto.getCardRefNo())
                    .paidAmount(purchasePaymentDto.getPaidAmount())
                    .paidDate(now)
                    .paymentId(savedPayment.getPaymentId())
                    .confirmPurchaseEntity(savedPayment.getConfirmPurchaseEntity())
                    .build();
            purchasePayCardRepo.save(aCardPayment);
        }  if (purchasePaymentDto.getPaymentType().equalsIgnoreCase("cash")) {
            PurchasePayCashEntity aCashPayment = PurchasePayCashEntity.builder()
                    .paidAmount(purchasePaymentDto.getPaidAmount())
                    .paidDate(now)
                    .paymentId(savedPayment.getPaymentId())
                    .confirmPurchaseEntity(savedPayment.getConfirmPurchaseEntity())
                    .build();
            purchasePayCashRepo.save(aCashPayment);
        } if (purchasePaymentDto.getPaymentType().equalsIgnoreCase("cheque")){
            PurchasePayChequeEntity aChequePayment = PurchasePayChequeEntity.builder()
                    .paidAmount(purchasePaymentDto.getPaidAmount())
                    .paidDate(now)
                    .chequeRefNo(purchasePaymentDto.getChequeRefNo())
                    .paymentId(savedPayment.getPaymentId())
                    .chequeDueDate(purchasePaymentDto.getChequeDueDate())
                    .confirmPurchaseEntity(savedPayment.getConfirmPurchaseEntity())
                    .build();
            purchasePayChequeRepo.save(aChequePayment);
        }
    }
    @Override
    public NonPaginatedResponse deletePurchaseInvoicePayment(Long paymentId) {
        return null;
    }

    @Override
    public NonPaginatedResponse updatePurchaseInvoicePayment(PurchasePaymentDto purchasePaymentDto) {
        return null;
    }

    @Override
    public NonPaginatedResponse getAllPurchaseInvoicePayments(Long purchaseId) {
        return null;
    }
}
