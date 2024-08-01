package com.appli.clcapi.paymentMethod.purchasePayMethods.repository;

import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasePayCardRepo extends JpaRepository<PurchasePayCardEntity, Long> {
}
