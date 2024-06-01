package com.appli.clcapi.payments.invoicePayments.confirmPayments.repository;

import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmPaymentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmPaymentsRepo extends JpaRepository<ConfirmPaymentsEntity, Long> {







}
