package com.appli.clcapi.tempInvoice.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface TempInvoiceService {
    NonPaginatedResponse createTempSalesInvoice(TempInvoiceDto tempInvoiceDto);
    ResponseEntity<String> delete(Long tempInvoiceId);
    ResponseEntity<String> update(TempInvoiceDto tempInvoiceDto);
    ResponseEntity<List<?>> getAll();
    ResponseEntity<ArrayList<?>> select(String existingChar);
    NonPaginatedResponse getTempInvoiceById(Long id);
}
