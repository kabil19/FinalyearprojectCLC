package com.appli.clcapi.payments.invoicePayments.confirmPayments.repository;

import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmSalesPaymentsEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConfirmSalesPaymentsRepo extends JpaRepository<ConfirmSalesPaymentsEntity, Long> {


List<ConfirmSalesPaymentsEntity> findByConfirmSalesInvoiceEntity_ConfirmInvoiceId(Long id);
List<ConfirmSalesPaymentsEntity> findByPaidDateBetween(LocalDateTime start, LocalDateTime end);
List<ConfirmSalesPaymentsEntity> findByConfirmSalesInvoiceEntity_ConfirmInvoiceIdAndPaidDateBetween(Long confirmInvoiceId, LocalDateTime start, LocalDateTime end);

List<ConfirmSalesPaymentsEntity> findByConfirmSalesInvoiceEntityInOrderByConfirmSalesInvoiceEntity(List<ConfirmSalesInvoiceEntity> listOfSales);




}
