package com.appli.clcapi.purchase.repository;

import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConfirmPurchaseRepo extends JpaRepository<ConfirmPurchaseEntity, Long> {

@Query("SELECT cpe FROM confirm_purchase_tbl cpe " +
        "JOIN cpe.vendorEntity ve " +
        "WHERE ve.vendorName LIKE %:searchCharacter% " +
        "OR CAST(cpe.purchaseInvoice AS string) LIKE %:searchCharacter% " +
        "OR FUNCTION('DATE_FORMAT', cpe.purchaseDate, '%Y-%m-%d %H:%i:%s')LIKE %:searchCharacter% ")
    List<ConfirmPurchaseEntity> searchByVendorNameOrPurchaseInvoiceOrPurchaseDate(@PathVariable("searchCharacter") String searchCharacter);

    List<ConfirmPurchaseEntity> findByPurchaseDateBetween(LocalDateTime start, LocalDateTime end);


}
