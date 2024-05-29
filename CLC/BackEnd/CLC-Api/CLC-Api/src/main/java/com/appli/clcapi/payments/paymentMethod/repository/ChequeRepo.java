package com.appli.clcapi.payments.paymentMethod.repository;

import com.appli.clcapi.payments.paymentMethod.entity.CashEntity;
import com.appli.clcapi.payments.paymentMethod.entity.ChequeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChequeRepo extends JpaRepository<ChequeEntity,Long> {

    List<ChequeEntity> findByTempInvoiceEntity_TempInvoiceId(Long invoiceId);

}
