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
import com.appli.clcapi.payments.purchasePayment.voucher.dto.VoucherDto;
import com.appli.clcapi.payments.purchasePayment.voucher.entity.VoucherEntity;
import com.appli.clcapi.payments.purchasePayment.voucher.repository.VoucherRepo;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import com.appli.clcapi.purchase.repository.ConfirmPurchaseRepo;
import com.appli.clcapi.vendor.entity.VendorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final VoucherRepo voucherRepo;

    @Override
    @Transactional
    public NonPaginatedResponse addToPurchaseInvoicePayment(PurchasePaymentDto purchasePaymentDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {
            NonPaginatedResponse paidAmountValidity = checkPaidAmountValid(purchasePaymentDto, response);
            if (paidAmountValidity.getStatus().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
                return paidAmountValidity;
            }
            NonPaginatedResponse paymentSourceStatus = checkPaymentSource(purchasePaymentDto, response);
            if (paymentSourceStatus.getStatus().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
                return paymentSourceStatus;
            }

            PurchasePaymentEntity aPayment = PurchasePaymentEntity.builder()
                    .paymentType(purchasePaymentDto.getPaymentType())
                    .paidDate(LocalDateTime.now())
                    .paidAmount(purchasePaymentDto.getPaidAmount())
                    .confirmPurchaseEntity(new ConfirmPurchaseEntity(purchasePaymentDto.getConfirmPurchaseDto()))
                    .vendorEntity(new VendorEntity(purchasePaymentDto.getVendorDto()))
                    .build();
            PurchasePaymentEntity savedPaymentEntity = purchasePaymentRepo.save(aPayment);

            addDetailsToTheRelevantPayMethod(purchasePaymentDto, savedPaymentEntity);
            VoucherEntity aVoucher = addToVoucherEntity(savedPaymentEntity, purchasePaymentDto);
            if (isFieldsOnConfirmPurchaseUpdated(purchasePaymentDto)) {
                response.setResult(new VoucherDto(aVoucher));
                response.setSuccessMessage("Payment has been made for the purchase invoice:- " + purchasePaymentDto.getConfirmPurchaseDto().getPurchaseInvoice());
                response.setStatus(HttpStatus.ACCEPTED);
                return response;
            }
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(List.of("Couldn't update the payment to the invoice"));
        } catch (Exception e) {
            response.setStatus(HttpStatus.BAD_REQUEST);
//            response.setErrors(List.of("Couldn't add the payment details"));
        }
        return response;
    }

    private NonPaginatedResponse checkPaymentSource(PurchasePaymentDto purchasePaymentDto, NonPaginatedResponse response) {
        if (purchasePaymentDto.getPaymentType().equalsIgnoreCase("card")) {
            boolean isCardExists = purchasePayCardRepo.existsById(purchasePaymentDto.getCardRefNo());
            if (isCardExists) {
                response.setErrors(List.of("Card ref No. already exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        if (purchasePaymentDto.getPaymentType().equalsIgnoreCase("cheque")) {
            boolean isChequeExists = purchasePayChequeRepo.existsById(purchasePaymentDto.getChequeRefNo());
            if (isChequeExists) {
                response.setErrors(List.of("Cheque ref No. already exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        response.setStatus(HttpStatus.OK);
        return response;
    }

    private VoucherEntity addToVoucherEntity(PurchasePaymentEntity savedPaymentEntity, PurchasePaymentDto purchasePaymentDto) {
        Date now = new Date();
        VoucherEntity aVoucher = VoucherEntity.builder()
                .confirmPurchaseEntity(savedPaymentEntity.getConfirmPurchaseEntity())
                .paidAmount(purchasePaymentDto.getPaidAmount())
                .paidDate(now)
                .paymentType(purchasePaymentDto.getPaymentType())
                .build();
        return voucherRepo.save(aVoucher);
    }

    private NonPaginatedResponse checkPaidAmountValid(PurchasePaymentDto purchasePaymentDto, NonPaginatedResponse response) {
        Optional<ConfirmPurchaseEntity> confirmPurchaseEntity = confirmPurchaseRepo.findById(
                purchasePaymentDto.getConfirmPurchaseDto().getConfirmPurchaseId()
        );
        if (confirmPurchaseEntity.isPresent()) {
            if (purchasePaymentDto.getPaidAmount() + confirmPurchaseEntity.get().getPaidAmount() > confirmPurchaseEntity.get().getNetAmount()) {
                response.setStatus(HttpStatus.BAD_REQUEST);
                response.setErrors(List.of("Amount exceeds the Total!"));
                return response;
            }
        } else {
            response.setErrors(List.of("Purchase Invoice is not exist!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }
        response.setStatus(HttpStatus.OK);
        return response;
    }

    private boolean isFieldsOnConfirmPurchaseUpdated(PurchasePaymentDto purchasePaymentDto) {
        Optional<ConfirmPurchaseEntity> confirmPurchaseEntity = confirmPurchaseRepo.findById(
                purchasePaymentDto.getConfirmPurchaseDto().getConfirmPurchaseId()
        );
        if (confirmPurchaseEntity.isPresent()) {
            double totalPaidAmount = confirmPurchaseEntity.get().getPaidAmount() + purchasePaymentDto.getPaidAmount();
            confirmPurchaseEntity.get().setPaidAmount(totalPaidAmount);
            if (totalPaidAmount == confirmPurchaseEntity.get().getNetAmount()) {
                confirmPurchaseEntity.get().setIsComplete(true);
            }
            confirmPurchaseRepo.save(confirmPurchaseEntity.get());
            return true;
        }
        return false;
    }

    private void addDetailsToTheRelevantPayMethod(PurchasePaymentDto purchasePaymentDto, PurchasePaymentEntity savedPayment) {
        LocalDateTime now = LocalDateTime.now();
        if (purchasePaymentDto.getPaymentType().equalsIgnoreCase("card")) {
            PurchasePayCardEntity aCardPayment = PurchasePayCardEntity.builder()
                    .cardRefNo(purchasePaymentDto.getCardRefNo())
                    .paidAmount(purchasePaymentDto.getPaidAmount())
                    .paidDate(now)
                    .paymentId(savedPayment.getPaymentId())
                    .confirmPurchaseEntity(savedPayment.getConfirmPurchaseEntity())
                    .build();
            purchasePayCardRepo.save(aCardPayment);
        }
        if (purchasePaymentDto.getPaymentType().equalsIgnoreCase("cash")) {
            PurchasePayCashEntity aCashPayment = PurchasePayCashEntity.builder()
                    .paidAmount(purchasePaymentDto.getPaidAmount())
                    .paidDate(now)
                    .paymentId(savedPayment.getPaymentId())
                    .confirmPurchaseEntity(savedPayment.getConfirmPurchaseEntity())
                    .build();
            purchasePayCashRepo.save(aCashPayment);
        }
        if (purchasePaymentDto.getPaymentType().equalsIgnoreCase("cheque")) {
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
    public NonPaginatedResponse getAllPurchaseInvoicePayments(Long purchaseInvoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<PurchasePaymentEntity> purchasePaymentEntities = purchasePaymentRepo.findByConfirmPurchaseEntity_ConfirmPurchaseId(purchaseInvoiceId);
            List<PurchasePaymentDto> purchasePaymentDtos = purchasePaymentEntities.stream()
                    .map(PurchasePaymentDto::new)
                    .toList();
            if(purchasePaymentDtos.isEmpty()){
                response.setErrors(List.of("No Payments has been made for the selected Purchase Invoice!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            response.setResult(purchasePaymentDtos);
            response.setSuccessMessage("Purchase Payment details are retrieved!");
            response.setStatus(HttpStatus.ACCEPTED);
            return response;
        } catch (Exception e) {
            response.setErrors(List.of("Could Retrieve Payments Details of the Purchase!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }


}
