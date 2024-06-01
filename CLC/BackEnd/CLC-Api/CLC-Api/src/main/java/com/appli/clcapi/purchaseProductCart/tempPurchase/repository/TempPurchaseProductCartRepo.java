package com.appli.clcapi.purchaseProductCart.tempPurchase.repository;

import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempPurchaseProductCartRepo extends JpaRepository<TempPurchaseProductCartEntity, Long> {
}
