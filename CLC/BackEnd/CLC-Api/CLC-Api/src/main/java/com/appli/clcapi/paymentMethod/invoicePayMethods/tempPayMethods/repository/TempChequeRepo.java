package com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository;

import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempChequeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempChequeRepo extends JpaRepository<TempChequeEntity,Long> {

List<TempChequeEntity> findByTempInvoiceEntity_TempInvoiceId(Long invoiceId);
}
