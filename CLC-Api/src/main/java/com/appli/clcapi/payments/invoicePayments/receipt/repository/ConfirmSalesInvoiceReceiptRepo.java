package com.appli.clcapi.payments.invoicePayments.receipt.repository;

import com.appli.clcapi.payments.invoicePayments.receipt.entity.ConfirmSalesInvoiceReceiptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmSalesInvoiceReceiptRepo extends JpaRepository<ConfirmSalesInvoiceReceiptEntity, Long> {
    List<ConfirmSalesInvoiceReceiptEntity> findByConfirmSalesInvoiceEntity_ConfirmInvoiceId(Long confirmInvoiceId);
}
