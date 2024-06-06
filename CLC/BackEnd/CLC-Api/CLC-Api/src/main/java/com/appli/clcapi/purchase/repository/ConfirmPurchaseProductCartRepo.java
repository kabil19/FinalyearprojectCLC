package com.appli.clcapi.purchase.repository;

import com.appli.clcapi.purchase.entity.ConfirmPurchaseProductCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConfirmPurchaseProductCartRepo extends JpaRepository<ConfirmPurchaseProductCartEntity, Long> {

    List<ConfirmPurchaseProductCartEntity> findByConfirmPurchaseEntity_ConfirmPurchaseId(Long purchaseId);
}
