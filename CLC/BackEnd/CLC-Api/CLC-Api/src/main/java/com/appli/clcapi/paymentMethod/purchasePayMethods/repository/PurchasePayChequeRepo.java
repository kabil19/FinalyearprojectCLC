package com.appli.clcapi.paymentMethod.purchasePayMethods.repository;

import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayChequeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasePayChequeRepo extends JpaRepository<PurchasePayChequeEntity, Long> {
}
