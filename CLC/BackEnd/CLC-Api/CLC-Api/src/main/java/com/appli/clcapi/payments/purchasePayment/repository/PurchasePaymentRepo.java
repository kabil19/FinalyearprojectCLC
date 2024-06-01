package com.appli.clcapi.payments.purchasePayment.repository;

import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasePaymentRepo extends JpaRepository<PurchasePaymentEntity, Long> {
}
