package com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.repository;

import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempCashEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempCashRepo extends JpaRepository<TempCashEntity, Long> {
List<TempCashEntity> findByTempInvoiceEntity_TempInvoiceId(Long invoiceId);

}
