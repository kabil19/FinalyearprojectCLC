package com.appli.clcapi.report.serviceImple;


import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmInvoiceRepo;
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


}
