package com.appli.clcapi.tempInvoice.service;

import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface TempInvoiceService {
    ResponseEntity<String> register(TempInvoiceDto tempInvoiceDto);
    ResponseEntity<String> delete(Long tempInvoiceId);
    ResponseEntity<String> update(TempInvoiceDto tempInvoiceDto);
    ResponseEntity<List<?>> getAll();
    ResponseEntity<ArrayList<?>> select(String existingChar);
}
