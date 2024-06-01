package com.appli.clcapi.purchase.repository;

import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempPurchaseRepo extends JpaRepository<TempPurchaseEntity, Long> {

}
