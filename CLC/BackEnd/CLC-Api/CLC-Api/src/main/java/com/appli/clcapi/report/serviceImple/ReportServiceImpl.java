package com.appli.clcapi.report.serviceImple;


import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.repository.ConfirmInvoiceRepo;
import com.appli.clcapi.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ConfirmInvoiceRepo confirmInvoiceRepo;

    @Override
    public NonPaginatedResponse selectSalesReportWithInRange(Date start, Date end) {
        NonPaginatedResponse response = new NonPaginatedResponse();
        LocalDateTime  startDateTime = convertToLocalDateTime(start);
        LocalDateTime endDateTime = convertToLocalDateTime(end);
        if (startDateTime.toLocalDate().equals(endDateTime.toLocalDate())) {
            endDateTime = endDateTime.with(LocalTime.MAX);
        }
        Date adjustedEndDate = convertToDate(endDateTime);

        List<ConfirmInvoiceEntity> confirmInvoiceEntities = confirmInvoiceRepo.findByDateBetween(start, adjustedEndDate);
        List<ConfirmInvoiceDto> confirmInvoiceDto = confirmInvoiceEntities.stream()
                .map(ConfirmInvoiceDto::new)
                .toList();
        response.setResult(confirmInvoiceDto);
        return response;
    }
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    private Date convertToDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
