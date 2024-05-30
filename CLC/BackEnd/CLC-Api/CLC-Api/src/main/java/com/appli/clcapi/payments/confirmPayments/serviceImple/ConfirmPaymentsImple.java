package com.appli.clcapi.payments.confirmPayments.serviceImple;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.payments.confirmPayments.dto.ConfirmPaymentsDto;
import com.appli.clcapi.payments.confirmPayments.entity.ConfirmPaymentsEntity;
import com.appli.clcapi.payments.confirmPayments.repository.ConfirmPaymentsRepo;
import com.appli.clcapi.payments.confirmPayments.service.ConfirmPaymentsService;
import com.appli.clcapi.paymentMethod.tempPayMethods.repository.TempCardRepo;
import com.appli.clcapi.paymentMethod.tempPayMethods.repository.TempCashRepo;
import com.appli.clcapi.paymentMethod.tempPayMethods.repository.TempChequeRepo;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfirmPaymentsImple implements ConfirmPaymentsService {

    private final ConfirmPaymentsRepo confirmPaymentsRepo;
    private final TempInvoiceRepo tempInvoiceRepo;
    private final TempCardRepo cardRepo;
    private final TempCashRepo cashRepo;
    private final TempChequeRepo chequeRepo;

    @Override
    @Transactional
   public NonPaginatedResponse addPayment(ConfirmPaymentsDto confirmPaymentsDto) {
       /* NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            ConfirmPaymentsEntity aPayment = ConfirmPaymentsEntity.builder()
                    .paymentId(confirmPaymentsDto.getPaymentId())
                    .paymentType(confirmPaymentsDto.getPaymentType())
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(confirmPaymentsDto.getPaidDate())
                    .salesInvoice(new TempInvoiceEntity(confirmPaymentsDto.getSalesInvoice()))
                    .build();
            var savedPaymentEntity =  paymentsRepo.save(aPayment);
            addDetailsToTheRelevantPayMethod(confirmPaymentsDto, savedPaymentEntity);
            Optional<TempInvoiceEntity> selectedSalesInvoice = tempInvoiceRepo.findById(confirmPaymentsDto.getSalesInvoice().getTempInvoiceId());
            selectedSalesInvoice.get().setPaidAmount(selectedSalesInvoice.get().getPaidAmount() + confirmPaymentsDto.getPaidAmount());
            tempInvoiceRepo.save(selectedSalesInvoice.get());

            response.setResult(null);
            response.setSuccessMessage(PaymentsConstants.PAYMENT_HAS_BEEN_ADDED);
            response.setStatus(HttpStatus.CREATED);

        } catch (Exception e) {
            response.setSuccessMessage(null);
            response.setErrors(Arrays.asList("An error occurred"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;*/
        return null;
    }

    private void addDetailsToTheRelevantPayMethod(ConfirmPaymentsDto confirmPaymentsDto, ConfirmPaymentsEntity savedPayment) {

       /* if(confirmPaymentsDto.getPaymentType().equalsIgnoreCase("card")){
            TempCardEntity aCardPayment = TempCardEntity.builder()
                    .cardRefNo(confirmPaymentsDto.getCardRefNo())
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(confirmPaymentsDto.getPaidDate())
                    .paymentId(savedPayment.getPaymentId())
                    .tempInvoiceEntity(savedPayment.getSalesInvoice())
                    .build();
            cardRepo.save(aCardPayment);
        } else if (confirmPaymentsDto.getPaymentType().equalsIgnoreCase("cash")) {
            TempCashEntity aCashPayment = TempCashEntity.builder()
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(confirmPaymentsDto.getPaidDate())
                    .paymentId(savedPayment.getPaymentId())
                    .tempInvoiceEntity(savedPayment.getSalesInvoice())
                    .build();
            cashRepo.save(aCashPayment);
        }else if (confirmPaymentsDto.getPaymentType().equalsIgnoreCase("cheque")){
            TempChequeEntity aChequePayment = TempChequeEntity.builder()
                    .paidAmount(confirmPaymentsDto.getPaidAmount())
                    .paidDate(confirmPaymentsDto.getPaidDate())
                    .chequeRefNo(confirmPaymentsDto.getChequeRefNo())
                    .paymentId(savedPayment.getPaymentId())
                    .chequeDueDate(confirmPaymentsDto.getChequeDueDate())
                    .tempInvoiceEntity(savedPayment.getSalesInvoice())
                    .build();
            chequeRepo.save(aChequePayment);
        }*/

    }

    @Override
    public NonPaginatedResponse deletePayment(Long payId) {
        return null;
    }

    @Override
    public NonPaginatedResponse updatePayment(ConfirmPaymentsDto confirmPaymentsDto) {
        return null;
    }

    @Override
    public NonPaginatedResponse getAllPayments(Long invoiceId) {
       /* NonPaginatedResponse response = new NonPaginatedResponse();
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
        return response;*/
        return null;
    }

    @Override
    public NonPaginatedResponse selectA_Payment() {return null;}

}
