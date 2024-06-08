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

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConfirmPaymentsImple implements ConfirmPaymentsService {

    private final ConfirmPaymentsRepo confirmPaymentsRepo;
    private final ConfirmInvoiceRepo confirmInvoiceRepo;
    private final ConfirmCardRepo confirmCardRepo;
    private final ConfirmCashRepo confirmCashRepo;
    private final ConfirmChequeRepo confirmChequeRepo;

    @Override
    @Transactional
   public NonPaginatedResponse addConfirmInvoiceToConfirmPayments(ConfirmPaymentsDto confirmPaymentsDto) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            ConfirmPaymentsEntity aPayment = ConfirmPaymentsEntity.builder()
                    .paymentId(confirmPaymentsDto.getPaymentId())
                    .paymentType(confirmPaymentsDto.getPaymentType())
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(confirmPaymentsDto.getPaidDate())
                    .confirmInvoice(new ConfirmInvoiceEntity(confirmPaymentsDto.getConfirmInvoiceDto()))
                    .build();
            var savedPaymentEntity =  confirmPaymentsRepo.save(aPayment);

            addDetailsToTheRelevantPayMethod(confirmPaymentsDto, savedPaymentEntity);

            Optional<ConfirmInvoiceEntity> selectedConfirmedInvoice = confirmInvoiceRepo.findById(confirmPaymentsDto.getConfirmInvoiceDto().getConfirmInvoiceId());
            double totalPaidAmount = selectedConfirmedInvoice.get().getPaidAmount() + confirmPaymentsDto.getPaidAmount();
            selectedConfirmedInvoice.get().setPaidAmount(totalPaidAmount);

            if(totalPaidAmount == (selectedConfirmedInvoice.get().getNetAmount())){
                selectedConfirmedInvoice.get().setIsComplete(true);
                confirmInvoiceRepo.save(selectedConfirmedInvoice.get());
            }

            response.setResult(confirmPaymentsDto);
            response.setSuccessMessage(PaymentsConstants.PAYMENT_HAS_BEEN_ADDED);
            response.setStatus(HttpStatus.CREATED);

        } catch (Exception e) {
            response.setSuccessMessage(null);
            response.setErrors(Arrays.asList("An error occurred"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    private void addDetailsToTheRelevantPayMethod(ConfirmPaymentsDto confirmPaymentsDto, ConfirmPaymentsEntity savedPayment) {

        if(confirmPaymentsDto.getPaymentType().equalsIgnoreCase("card")){
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
        }else if (confirmPaymentsDto.getPaymentType().equalsIgnoreCase("cheque")){
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
    }

    @Override
    public NonPaginatedResponse getAllPayments(Long invoiceId) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try{
            List<ConfirmPaymentsEntity> paymentsEntities = paymentsRepo.findBySalesInvoice_TempInvoiceId(invoiceId);
            List<ConfirmPaymentsDto> confirmPaymentsDtos = new ArrayList<>();
            for (ConfirmPaymentsEntity aPay :paymentsEntities){
                ConfirmPaymentsDto aPaymentDto = new ConfirmPaymentsDto(aPay);
                confirmPaymentsDtos.add(aPaymentDto);
            }
            response.setResult(confirmPaymentsDtos);
            response.setStatus(HttpStatus.OK);
            response.setSuccessMessage("Data is retrieved");
        }catch (Exception e){
            response.setStatus(HttpStatus.BAD_REQUEST);
            response.setErrors(Arrays.asList("Couldn't find anything"));
        }
        return response;
        return null;
    }

    @Override
    public NonPaginatedResponse selectA_Payment() {return null;}*/

}
