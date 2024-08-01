package com.appli.clcapi.paymentMethod.purchasePayMethods.repository;

import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayCashEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasePayCashRepo extends JpaRepository<PurchasePayCashEntity, Long> {
}
