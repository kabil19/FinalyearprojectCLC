package com.appli.clcapi.report.serviceImple;


import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmInvoiceRepo;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmPaymentsDto;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmPaymentsEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.repository.ConfirmPaymentsRepo;
import com.appli.clcapi.payments.purchasePayment.dto.PurchasePaymentDto;
import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import com.appli.clcapi.payments.purchasePayment.repository.PurchasePaymentRepo;
import com.appli.clcapi.purchase.dto.ConfirmPurchaseDto;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import com.appli.clcapi.purchase.repository.ConfirmPurchaseRepo;
import com.appli.clcapi.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ConfirmInvoiceRepo confirmInvoiceRepo;
    private final ConfirmPurchaseRepo confirmPurchaseRepo;
    private final PurchasePaymentRepo purchasePaymentRepo;
    private final ConfirmPaymentsRepo confirmSalesInvoicePaymentsRepo;

    @Override
    public NonPaginatedResponse selectSalesReportWithInRange(LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {

            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            List<ConfirmInvoiceEntity> confirmInvoiceEntities = confirmInvoiceRepo.findByDateBetween(start, end);
            List<ConfirmInvoiceDto> confirmInvoiceDto = confirmInvoiceEntities.stream()
                    .map(ConfirmInvoiceDto::new)
                    .toList();
            if (confirmInvoiceDto.isEmpty()) {
                response.setErrors(List.of("No Sales Reports exist with-in the given range! "));
                return response;
            }
            response.setResult(confirmInvoiceDto);
            response.setSuccessMessage("Sales reports are retrieved from the given range! ");
//            response.setSuccessMessage("Sales reports are retrieved from the given range of! " + start + " To " + end);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't Retrieve any Reports!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public NonPaginatedResponse selectPurchaseReportWithInRange(LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {

            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            List<ConfirmPurchaseEntity> confirmPurchaseEntities = confirmPurchaseRepo.findByPurchaseDateBetween(start, end);
            List<ConfirmPurchaseDto> confirmInvoiceDto = confirmPurchaseEntities.stream()
                    .map(ConfirmPurchaseDto::new)
                    .toList();
            if (confirmInvoiceDto.isEmpty()) {
                response.setErrors(List.of("No Purchase Reports exist with-in the given range! "));
                return response;
            }
            response.setResult(confirmInvoiceDto);
            response.setSuccessMessage("Purchase reports are retrieved from the given range! ");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't Retrieve any Reports!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public NonPaginatedResponse selectAllPurchaseInvoicePaymentsWithInRange(LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {

            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            List<PurchasePaymentEntity> purchasePaymentEntities = purchasePaymentRepo.findByPaidDateBetween(start, end);
            List<PurchasePaymentDto> purchasePaymentDtos = purchasePaymentEntities.stream()
                    .map(PurchasePaymentDto::new)
                    .toList();
            if (purchasePaymentDtos.isEmpty()) {
                response.setErrors(List.of("No Purchase payment Reports exist with-in the given range! "));
                return response;
            }
            response.setResult(purchasePaymentDtos);
            response.setSuccessMessage("Purchase payment reports are retrieved from the given range! ");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't Retrieve any Purchase payment Reports!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }
    @Override
    public NonPaginatedResponse selectAllSalesInvoicePaymentsWithInRange(LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {

            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            List<ConfirmPaymentsEntity> salesInvoicePaymentsList = confirmSalesInvoicePaymentsRepo.findByPaidDateBetween(start, end);
            List<ConfirmPaymentsDto> salesInvoicePaymentsDtoList = salesInvoicePaymentsList.stream()
                    .map(ConfirmPaymentsDto::new)
                    .toList();
            if (salesInvoicePaymentsDtoList.isEmpty()) {
                response.setErrors(List.of("No Sales Invoice payment Reports exist with-in the given range! "));
                return response;
            }
            response.setResult(salesInvoicePaymentsDtoList);
            response.setSuccessMessage("Sales Invoice payment reports are retrieved from the given range! ");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't Retrieve any Sales Invoice payment Reports!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }



}
