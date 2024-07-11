package com.appli.clcapi.payments.invoicePayments.confirmPayments.repository;

import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmPaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConfirmPaymentsRepo extends JpaRepository<ConfirmPaymentsEntity, Long> {


List<ConfirmPaymentsEntity> findByConfirmInvoice_ConfirmInvoiceId(Long id);
List<ConfirmPaymentsEntity> findByPaidDateBetween(LocalDateTime start, LocalDateTime end);
List<ConfirmPaymentsEntity> findByConfirmInvoice_ConfirmInvoiceIdAndPaidDateBetween(Long confirmInvoiceId,LocalDateTime start, LocalDateTime end);





}
