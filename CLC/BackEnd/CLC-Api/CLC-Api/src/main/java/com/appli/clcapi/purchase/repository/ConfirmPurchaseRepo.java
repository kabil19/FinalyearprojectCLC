package com.appli.clcapi.purchase.repository;

import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmPurchaseRepo extends JpaRepository<ConfirmPurchaseEntity, Long> {
}
