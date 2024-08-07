package com.appli.clcapi.payments.invoicePayments.confirmPayments.serviceImple;


import com.appli.clcapi.common.constants.PaymentsConstants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmSalesInvoiceRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmSalesInvoiceChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmCardRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmCashRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmSalesPayChequeRepo;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmSalesPaymentsDto;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmSalesPaymentsEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.repository.ConfirmSalesPaymentsRepo;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.service.ConfirmPaymentsService;
import com.appli.clcapi.payments.invoicePayments.receipt.dto.ConfirmSalesInvoiceReceiptDto;
import com.appli.clcapi.payments.invoicePayments.receipt.entity.ConfirmSalesInvoiceReceiptEntity;
import com.appli.clcapi.payments.invoicePayments.receipt.repository.ConfirmSalesInvoiceReceiptRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmSalesPaymentsImpl implements ConfirmPaymentsService {

    private final ConfirmSalesPaymentsRepo confirmSalesInvoicePaymentsRepo;
    private final ConfirmSalesInvoiceRepo confirmSalesInvoiceRepo;
    private final ConfirmCardRepo confirmCardRepo;
    private final ConfirmCashRepo confirmCashRepo;
    private final ConfirmSalesPayChequeRepo confirmSalesPayChequeRepo;
    private final ConfirmSalesInvoiceReceiptRepo confirmSalesInvoiceReceiptRepo;

    @Override
    @Transactional
    public NonPaginatedResponse makePaymentToConfirmInvoice(ConfirmSalesPaymentsDto confirmSalesPaymentsDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {
            Optional<ConfirmSalesInvoiceEntity> confirmedInvoice = confirmSalesInvoiceRepo.findById(confirmSalesPaymentsDto.getConfirmInvoiceDto().getConfirmInvoiceId());
            if(confirmedInvoice.isPresent()) {
                if ((confirmedInvoice.get().getPaidAmount()) + confirmSalesPaymentsDto.getPaidAmount() > confirmedInvoice.get().getNetAmount()) {
                    response.setErrors(List.of("Payment exceeds the total!"));
                    response.setStatus(HttpStatus.BAD_REQUEST);
                    return response;
                }
            }
            NonPaginatedResponse paymentSourceStatus = checkPaymentSource(confirmSalesPaymentsDto, response);
            if (paymentSourceStatus.getStatus().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
                return paymentSourceStatus;
            }

            ConfirmSalesPaymentsEntity aPayment = ConfirmSalesPaymentsEntity.builder()
                    .paymentId(confirmSalesPaymentsDto.getPaymentId())
                    .paymentType(confirmSalesPaymentsDto.getPaymentType())
                    .paidAmount(confirmSalesPaymentsDto.getPaidAmount())
                    .paidDate(LocalDateTime.now())
                    .confirmSalesInvoiceEntity(new ConfirmSalesInvoiceEntity(confirmSalesPaymentsDto.getConfirmInvoiceDto()))
                    .build();
            var savedPaymentEntity = confirmSalesInvoicePaymentsRepo.save(aPayment);

            addDetailsToTheRelevantPayMethod(confirmSalesPaymentsDto, savedPaymentEntity);


            Optional<ConfirmSalesInvoiceEntity> selectedConfirmedInvoice = confirmSalesInvoiceRepo.findById(confirmSalesPaymentsDto.getConfirmInvoiceDto().getConfirmInvoiceId());
            if(selectedConfirmedInvoice.isPresent()) {
                double totalPaidAmount = selectedConfirmedInvoice.get().getPaidAmount() + confirmSalesPaymentsDto.getPaidAmount();
                selectedConfirmedInvoice.get().setPaidAmount(totalPaidAmount);

                if (totalPaidAmount == (selectedConfirmedInvoice.get().getNetAmount())) {
                    selectedConfirmedInvoice.get().setIsComplete(true);
                    confirmSalesInvoiceRepo.save(selectedConfirmedInvoice.get());
                }

                ConfirmSalesInvoiceReceiptEntity aReceipt = addToConfirmSalesInvoiceReceipt(confirmSalesPaymentsDto, selectedConfirmedInvoice.get());
                ConfirmSalesInvoiceReceiptDto aReceiptDto = new ConfirmSalesInvoiceReceiptDto(aReceipt);
                response.setResult(aReceiptDto);
                response.setSuccessMessage(PaymentsConstants.PAYMENT_HAS_BEEN_ADDED);
                response.setStatus(HttpStatus.CREATED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setSuccessMessage(null);
//            response.setErrors(List.of("An error occurred"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private NonPaginatedResponse checkPaymentSource(ConfirmSalesPaymentsDto confirmSalesPaymentsDto, NonPaginatedResponse response) {
        if (confirmSalesPaymentsDto.getPaymentType().equalsIgnoreCase("card")) {
            boolean isExists = confirmCardRepo.existsById(confirmSalesPaymentsDto.getCardRefNo());
            if (isExists) {
                response.setErrors(List.of("Card ref No. already exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        if (confirmSalesPaymentsDto.getPaymentType().equalsIgnoreCase("cheque")) {
            boolean isExists = confirmSalesPayChequeRepo.existsById(confirmSalesPaymentsDto.getChequeRefNo());
            if (isExists) {
                response.setErrors(List.of("Cheque ref No. already exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        response.setStatus(HttpStatus.OK);
        return response;
    }

    private ConfirmSalesInvoiceReceiptEntity addToConfirmSalesInvoiceReceipt(ConfirmSalesPaymentsDto confirmSalesPaymentsDto, ConfirmSalesInvoiceEntity confirmInvoice) {
        Date now = new Date();
        ConfirmSalesInvoiceReceiptEntity aReceipt = ConfirmSalesInvoiceReceiptEntity.builder()
                .confirmSalesInvoiceEntity(confirmInvoice)
                .paymentType(confirmSalesPaymentsDto.getPaymentType())
                .paidAmount(confirmSalesPaymentsDto.getPaidAmount())
                .paidDate(now)
                .build();
        return confirmSalesInvoiceReceiptRepo.save(aReceipt);
    }

    private void addDetailsToTheRelevantPayMethod(ConfirmSalesPaymentsDto confirmSalesPaymentsDto, ConfirmSalesPaymentsEntity savedPayment) {

        if (confirmSalesPaymentsDto.getPaymentType().equalsIgnoreCase("card")) {

            ConfirmCardEntity aCardPayment = ConfirmCardEntity.builder()
                    .cardRefNo(confirmSalesPaymentsDto.getCardRefNo())
                    .paidAmount(confirmSalesPaymentsDto.getPaidAmount())
                    .paidDate(confirmSalesPaymentsDto.getPaidDate())
                    .paymentId(savedPayment.getPaymentId())
                    .confirmSalesInvoiceEntity(savedPayment.getConfirmSalesInvoiceEntity())
                    .build();
            confirmCardRepo.save(aCardPayment);
        } else if (confirmSalesPaymentsDto.getPaymentType().equalsIgnoreCase("cash")) {
            ConfirmCashEntity aCashPayment = ConfirmCashEntity.builder()
                    .paidAmount(confirmSalesPaymentsDto.getPaidAmount())
                    .paidDate(confirmSalesPaymentsDto.getPaidDate())
                    .paymentId(savedPayment.getPaymentId())
                    .confirmSalesInvoiceEntity(savedPayment.getConfirmSalesInvoiceEntity())
                    .build();
            confirmCashRepo.save(aCashPayment);
        } else if (confirmSalesPaymentsDto.getPaymentType().equalsIgnoreCase("cheque")) {
            ConfirmSalesInvoiceChequeEntity aChequePayment = ConfirmSalesInvoiceChequeEntity.builder()
                    .paidAmount(confirmSalesPaymentsDto.getPaidAmount())
                    .paidDate(confirmSalesPaymentsDto.getPaidDate())
                    .chequeRefNo(confirmSalesPaymentsDto.getChequeRefNo())
                    .paymentId(savedPayment.getPaymentId())
                    .chequeDueDate(confirmSalesPaymentsDto.getChequeDueDate())
                    .confirmSalesInvoiceEntity(savedPayment.getConfirmSalesInvoiceEntity())
                    .build();
            confirmSalesPayChequeRepo.save(aChequePayment);
        }
    }


    @Override
    public NonPaginatedResponse getAllConfirmPaymentsOfConfirmInvoice(Long confirmSalesInvoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmSalesPaymentsEntity> confirmedSalesInvoicePaymentRecords = confirmSalesInvoicePaymentsRepo.findByConfirmSalesInvoiceEntity_ConfirmInvoiceId(confirmSalesInvoiceId);
            List<ConfirmSalesPaymentsDto> aConfirmedSalesInvoicePayment = confirmedSalesInvoicePaymentRecords.stream()
                    .map(ConfirmSalesPaymentsDto::new)
                    .toList();
            if(aConfirmedSalesInvoicePayment.isEmpty()){
                response.setErrors(List.of("No Payments has been made for the selected Sales Invoice!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            response.setResult(aConfirmedSalesInvoicePayment);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Payment Records for the  Sales invoice are Retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve the Payment Records for the Sales invoice!"));
        }
        return response;
    }



}
