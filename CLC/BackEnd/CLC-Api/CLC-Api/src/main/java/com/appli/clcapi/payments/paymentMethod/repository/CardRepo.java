package com.appli.clcapi.payments.paymentMethod.repository;

import com.appli.clcapi.payments.paymentMethod.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepo extends JpaRepository<CardEntity,Long> {
    List<CardEntity> findByTempInvoiceEntity_TempInvoiceIdOrConfirmInvoiceEntity_ConfirmInvoiceId(Long invoiceId, Long InvoiceId);
    List<CardEntity> findByTempInvoiceEntity_TempInvoiceId(Long invoiceId );
}
