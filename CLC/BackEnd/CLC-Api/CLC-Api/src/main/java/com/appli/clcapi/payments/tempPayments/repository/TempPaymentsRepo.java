package com.appli.clcapi.payments.tempPayments.repository;

import com.appli.clcapi.payments.tempPayments.entity.TempPaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TempPaymentsRepo extends JpaRepository<TempPaymentsEntity, Long> {

List<TempPaymentsEntity> findByTempSalesInvoice_TempInvoiceId(Long invoiceId);



}
