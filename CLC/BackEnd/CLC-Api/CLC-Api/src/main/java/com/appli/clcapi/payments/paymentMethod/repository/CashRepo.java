package com.appli.clcapi.payments.paymentMethod.repository;

import com.appli.clcapi.payments.paymentMethod.entity.CashEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CashRepo extends JpaRepository<CashEntity, Long> {

    List<CashEntity> findByTempInvoiceEntity_TempInvoiceId(Long invoiceId);
}
