package com.appli.clcapi.tempInvoice.service;

import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;

import java.util.ArrayList;
import java.util.List;

public interface TempInvoiceService {
    String register(TempInvoiceDto tempInvoiceDto);
    String delete(Long tempInvoiceId);
    String update(TempInvoiceDto tempInvoiceDto);
    List<TempInvoiceDto> getAll();
    ArrayList<TempInvoiceDto> select(String existingChar);
}
