package com.appli.clcapi.report.serviceImple;


import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmSalesInvoiceRepo;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmSalesPaymentsDto;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmSalesPaymentsEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.repository.ConfirmSalesPaymentsRepo;
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

import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ConfirmSalesInvoiceRepo confirmSalesInvoiceRepo;
    private final ConfirmPurchaseRepo confirmPurchaseRepo;
    private final PurchasePaymentRepo purchasePaymentRepo;
    private final ConfirmSalesPaymentsRepo confirmSalesInvoicePaymentsRepo;

    @Override
    public NonPaginatedResponse selectSalesReportWithInRange(LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {

            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            List<ConfirmSalesInvoiceEntity> confirmInvoiceEntities = confirmSalesInvoiceRepo.findByDateBetween(start, end);
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
            List<ConfirmSalesPaymentsEntity> salesInvoicePaymentsList = confirmSalesInvoicePaymentsRepo.findByPaidDateBetween(start, end);
            List<ConfirmSalesPaymentsDto> salesInvoicePaymentsDtoList = salesInvoicePaymentsList.stream()
                    .map(ConfirmSalesPaymentsDto::new)
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

    @Override
    public NonPaginatedResponse selectAllPaymentsOfTheSalesInvoiceWithInTheRange(Long confirmSalesInvoiceId, LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {

            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            List<ConfirmSalesPaymentsEntity> salesInvoicePaymentsList = confirmSalesInvoicePaymentsRepo.findByConfirmInvoice_ConfirmInvoiceIdAndPaidDateBetween(confirmSalesInvoiceId, start, end);
            List<ConfirmSalesPaymentsDto> salesInvoicePaymentsDtoList = salesInvoicePaymentsList.stream()
                    .map(ConfirmSalesPaymentsDto::new)
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

    @Override
    public NonPaginatedResponse selectAllPaymentsOfThePurchaseInvoiceWithInTheRange(Long purchaseInvoiceId, LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();

        try {

            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            List<PurchasePaymentEntity> purchaseInvoicePaymentsList = purchasePaymentRepo.findByConfirmPurchaseEntity_ConfirmPurchaseIdAndPaidDateBetween(purchaseInvoiceId, start, end);
            List<PurchasePaymentDto> purchaseInvoicePaymentsDtoList = purchaseInvoicePaymentsList.stream()
                    .map(PurchasePaymentDto::new)
                    .toList();
            if (purchaseInvoicePaymentsDtoList.isEmpty()) {
                response.setErrors(List.of("No Purchase Invoice payment Reports exist with-in the given range! "));
                return response;
            }
            response.setResult(purchaseInvoicePaymentsDtoList);
            response.setSuccessMessage("Purchase Invoice payment reports are retrieved from the given range! ");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(List.of("Couldn't Retrieve any Purchase Invoice payment Reports!"));
            response.setStatus(HttpStatus.BAD_REQUEST);
        }
        return response;
    }

    @Override
    public NonPaginatedResponse selectAllPaymentsOfaCustomerWithInRange(Long custId, LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            //finds out the customer's invoices that has been confirmed within the provided range
            List<ConfirmSalesInvoiceEntity> salesInvoiceList = confirmSalesInvoiceRepo
                    .findByCustomer_CustIdAndDateBetweenOrderByConfirmInvoiceId(custId,start,end);
            if (salesInvoiceList.isEmpty()) {
                response.setErrors(List.of("No Sales Invoice exist for the selected Customer!"));
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }


            List<ConfirmInvoiceDto> salesInvoiceData = salesInvoiceList.stream()
                    .map(ConfirmInvoiceDto::new)
                    .toList();
            List<ConfirmSalesPaymentsEntity> listOfSalesPayments =  confirmSalesInvoicePaymentsRepo.findByConfirmInvoiceInOrderByConfirmInvoice(salesInvoiceList);

            List<ConfirmSalesPaymentsDto> paymentsListDtoOfSales = listOfSalesPayments.stream()
                    .map(ConfirmSalesPaymentsDto::new)
                    .toList();

            Map<String, Object> customerReportRes = new HashMap<>();
            customerReportRes.put("salesInvoiceData", salesInvoiceData);
            customerReportRes.put("paymentsOfTheSales", paymentsListDtoOfSales);



            response.setResult(customerReportRes);
            response.setSuccessMessage("The Selected Customer's Payments within the selected range for the Purchase Invoice are retrieved!");
            response.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }
        return response;
    }

    @Override

    public NonPaginatedResponse selectAllPaymentsOfaVendorWithInRange(Long vendorId, LocalDateTime start, LocalDateTime end) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        try {
            start = start.with(LocalTime.MIN);
            end = end.with(LocalTime.MAX);
            List<ConfirmPurchaseEntity> purchaseInvoiceList = confirmPurchaseRepo
                    .findByVendorEntity_VendorIdAndPurchaseDateBetweenOrderByConfirmPurchaseId(vendorId, start, end);
            if (purchaseInvoiceList.isEmpty()) {
                response.setErrors(List.of("No Purchase Invoice exist for the selected Customer!"));
                response.setStatus(HttpStatus.NOT_FOUND);
                return response;
            }
            List<ConfirmPurchaseDto> purchaseInvoiceData = purchaseInvoiceList.stream()
                    .map(ConfirmPurchaseDto::new)
                    .toList();
            List<PurchasePaymentEntity> listOfPurchasePayments =  purchasePaymentRepo.findByConfirmPurchaseEntityInOrderByConfirmPurchaseEntity(purchaseInvoiceList);

            List<PurchasePaymentDto> paymentsListDtoOfPurchase = listOfPurchasePayments.stream()
                    .map(PurchasePaymentDto::new)
                    .toList();

            Map<String, Object> vendorReportRes = new HashMap<>();
            vendorReportRes.put("purchaseInvoiceData", purchaseInvoiceData);
            vendorReportRes.put("paymentsOfThePurchase", paymentsListDtoOfPurchase);



                response.setResult(vendorReportRes);
                response.setSuccessMessage("The Selected Vendor's Payments within the selected range for the Purchase Invoice are retrieved!");
                response.setStatus(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST);
            return response;
        }
        return response;
    }

}


