package com.appli.clcapi.payments.invoicePayments.confirmPayments.serviceImple;


import com.appli.clcapi.common.constants.PaymentsConstants;
import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmInvoiceRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmChequeEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmCardRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmCashRepo;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository.ConfirmChequeRepo;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmPaymentsDto;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmPaymentsEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.repository.ConfirmPaymentsRepo;
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
public class ConfirmPaymentsImple implements ConfirmPaymentsService {

    private final ConfirmPaymentsRepo confirmSalesInvoicePaymentsRepo;
    private final ConfirmInvoiceRepo confirmInvoiceRepo;
    private final ConfirmCardRepo confirmCardRepo;
    private final ConfirmCashRepo confirmCashRepo;
    private final ConfirmChequeRepo confirmChequeRepo;
    private final ConfirmSalesInvoiceReceiptRepo confirmSalesInvoiceReceiptRepo;

    @Override
    @Transactional
    public NonPaginatedResponse makePaymentToConfirmInvoice(ConfirmPaymentsDto confirmPaymentsDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        Date now = new Date();
        try {
            Optional<ConfirmInvoiceEntity> confirmedInvoice = confirmInvoiceRepo.findById(confirmPaymentsDto.getConfirmInvoiceDto().getConfirmInvoiceId());
            if ((confirmedInvoice.get().getPaidAmount()) + confirmPaymentsDto.getPaidAmount() > confirmedInvoice.get().getNetAmount()) {
                response.setErrors(List.of("Payment exceeds the total!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
            NonPaginatedResponse paymentSourceStatus = checkPaymentSource(confirmPaymentsDto, response);
            if (paymentSourceStatus.getStatus().isSameCodeAs(HttpStatus.BAD_REQUEST)) {
                return paymentSourceStatus;
            }

            ConfirmPaymentsEntity aPayment = ConfirmPaymentsEntity.builder()
                    .paymentId(confirmPaymentsDto.getPaymentId())
                    .paymentType(confirmPaymentsDto.getPaymentType())
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(LocalDateTime.now())
                    .confirmInvoice(new ConfirmInvoiceEntity(confirmPaymentsDto.getConfirmInvoiceDto()))
                    .build();
            var savedPaymentEntity = confirmSalesInvoicePaymentsRepo.save(aPayment);

            addDetailsToTheRelevantPayMethod(confirmPaymentsDto, savedPaymentEntity);


            Optional<ConfirmInvoiceEntity> selectedConfirmedInvoice = confirmInvoiceRepo.findById(confirmPaymentsDto.getConfirmInvoiceDto().getConfirmInvoiceId());
            double totalPaidAmount = selectedConfirmedInvoice.get().getPaidAmount() + confirmPaymentsDto.getPaidAmount();
            selectedConfirmedInvoice.get().setPaidAmount(totalPaidAmount);

            if (totalPaidAmount == (selectedConfirmedInvoice.get().getNetAmount())) {
                selectedConfirmedInvoice.get().setIsComplete(true);
                confirmInvoiceRepo.save(selectedConfirmedInvoice.get());
            }
            ConfirmSalesInvoiceReceiptEntity aReceipt = addToConfirmSalesInvoiceReceipt(confirmPaymentsDto, selectedConfirmedInvoice.get());
            ConfirmSalesInvoiceReceiptDto aReceiptDto = new ConfirmSalesInvoiceReceiptDto(aReceipt);
            response.setResult(aReceiptDto);
            response.setSuccessMessage(PaymentsConstants.PAYMENT_HAS_BEEN_ADDED);
            response.setStatus(HttpStatus.CREATED);
        } catch (Exception e) {
            response.setSuccessMessage(null);
//            response.setErrors(List.of("An error occurred"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private NonPaginatedResponse checkPaymentSource(ConfirmPaymentsDto confirmPaymentsDto, NonPaginatedResponse response) {
        if (confirmPaymentsDto.getPaymentType().equalsIgnoreCase("card")) {
            boolean isExists = confirmCardRepo.existsById(confirmPaymentsDto.getCardRefNo());
            if (isExists) {
                response.setErrors(List.of("Card ref No. already exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        if (confirmPaymentsDto.getPaymentType().equalsIgnoreCase("cheque")) {
            boolean isExists = confirmChequeRepo.existsById(confirmPaymentsDto.getChequeRefNo());
            if (isExists) {
                response.setErrors(List.of("Cheque ref No. already exists!"));
                response.setStatus(HttpStatus.BAD_REQUEST);
                return response;
            }
        }
        response.setStatus(HttpStatus.OK);
        return response;
    }

    private ConfirmSalesInvoiceReceiptEntity addToConfirmSalesInvoiceReceipt(ConfirmPaymentsDto confirmPaymentsDto, ConfirmInvoiceEntity confirmInvoice) {
        Date now = new Date();
        ConfirmSalesInvoiceReceiptEntity aReceipt = ConfirmSalesInvoiceReceiptEntity.builder()
                .confirmInvoiceEntity(confirmInvoice)
                .paymentType(confirmPaymentsDto.getPaymentType())
                .paidAmount(confirmPaymentsDto.getPaidAmount())
                .paidDate(now)
                .build();
        return confirmSalesInvoiceReceiptRepo.save(aReceipt);
    }

    private void addDetailsToTheRelevantPayMethod(ConfirmPaymentsDto confirmPaymentsDto, ConfirmPaymentsEntity savedPayment) {

        if (confirmPaymentsDto.getPaymentType().equalsIgnoreCase("card")) {

            ConfirmCardEntity aCardPayment = ConfirmCardEntity.builder()
                    .cardRefNo(confirmPaymentsDto.getCardRefNo())
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(confirmPaymentsDto.getPaidDate())
                    .paymentId(savedPayment.getPaymentId())
                    .confirmInvoiceEntity(savedPayment.getConfirmInvoice())
                    .build();
            confirmCardRepo.save(aCardPayment);
        } else if (confirmPaymentsDto.getPaymentType().equalsIgnoreCase("cash")) {
            ConfirmCashEntity aCashPayment = ConfirmCashEntity.builder()
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(confirmPaymentsDto.getPaidDate())
                    .paymentId(savedPayment.getPaymentId())
                    .confirmInvoiceEntity(savedPayment.getConfirmInvoice())
                    .build();
            confirmCashRepo.save(aCashPayment);
        } else if (confirmPaymentsDto.getPaymentType().equalsIgnoreCase("cheque")) {
            ConfirmChequeEntity aChequePayment = ConfirmChequeEntity.builder()
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(confirmPaymentsDto.getPaidDate())
                    .chequeRefNo(confirmPaymentsDto.getChequeRefNo())
                    .paymentId(savedPayment.getPaymentId())
                    .chequeDueDate(confirmPaymentsDto.getChequeDueDate())
                    .confirmInvoiceEntity(savedPayment.getConfirmInvoice())
                    .build();
            confirmChequeRepo.save(aChequePayment);
        }
    }

   /* @Override
    public NonPaginatedResponse deletePayment(Long payId) {
        return null;
    }

    @Override
    public NonPaginatedResponse updatePayment(ConfirmPaymentsDto confirmPaymentsDto) {
        return null;
    }*/

    @Override
    public NonPaginatedResponse getAllConfirmPaymentsOfConfirmInvoice(Long confirmSalesInvoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            List<ConfirmPaymentsEntity> confirmedSalesInvoicePaymentRecords = confirmSalesInvoicePaymentsRepo.findByConfirmInvoice_ConfirmInvoiceId(confirmSalesInvoiceId);
            List<ConfirmPaymentsDto> aConfirmedSalesInvoicePayment = confirmedSalesInvoicePaymentRecords.stream()
                    .map(ConfirmPaymentsDto::new)
                    .toList();

            response.setResult(aConfirmedSalesInvoicePayment);
            response.setStatus(HttpStatus.ACCEPTED);
            response.setSuccessMessage("Payment Records for the confirm sales invoice are Retrieved!");
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't retrieve the Payment Records for the confirm sales invoice!"));
        }
        return response;
    }

   /* @Override
    public NonPaginatedResponse selectA_Payment() {return null;}*/

}
