package com.appli.clcapi.payments.purchasePayment.repository;

import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchasePaymentRepo extends JpaRepository<PurchasePaymentEntity, Long> {

    List<PurchasePaymentEntity> findByConfirmPurchaseEntity_ConfirmPurchaseId(long confirmPurchaseInvoiceId);
    List<PurchasePaymentEntity> findByPaidDateBetween(LocalDateTime start, LocalDateTime end);
    List<PurchasePaymentEntity> findByConfirmPurchaseEntity_ConfirmPurchaseIdAndPaidDateBetween(long purchaseId,LocalDateTime start, LocalDateTime end);

    List<PurchasePaymentEntity> findByConfirmPurchaseEntityInOrderByConfirmPurchaseEntity(List<ConfirmPurchaseEntity> listOfPurchase);
}
