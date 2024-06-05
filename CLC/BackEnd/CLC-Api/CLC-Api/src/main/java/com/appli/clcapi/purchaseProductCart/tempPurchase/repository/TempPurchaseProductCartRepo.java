package com.appli.clcapi.purchaseProductCart.tempPurchase.repository;

import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TempPurchaseProductCartRepo extends JpaRepository<TempPurchaseProductCartEntity, Long> {
    Optional<TempPurchaseProductCartEntity> findByStockEntity_StockIdAndTempPurchaseEntity_PurchaseId(Long stockId, Long purchaseId);
//Optional<TempPurchaseProductCartEntity> findByTempPurchaseEntity_PurchaseId();

//    List<TempPurchaseProductCartEntity> findByStockEntityContainingIgnoreCase(String existingChar);
    List<TempPurchaseProductCartEntity> findByStockEntity_ItemNameContaining(String existingChar);



}
