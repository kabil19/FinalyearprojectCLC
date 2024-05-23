package com.appli.clcapi.payments.repository;

import com.appli.clcapi.payments.entity.PaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentsRepo extends JpaRepository<PaymentsEntity, Long> {


    List<PaymentsEntity> findBySellInvoice_TempInvoiceId(Long invoiceId);
}
