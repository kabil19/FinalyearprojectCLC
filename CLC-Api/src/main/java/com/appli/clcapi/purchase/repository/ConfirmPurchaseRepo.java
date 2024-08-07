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

@Query("SELECT confirmPurchaseInvoiceEntity FROM confirm_purchase_tbl confirmPurchaseInvoiceEntity " +
        "JOIN confirmPurchaseInvoiceEntity.vendorEntity ve " +
        "WHERE ve.vendorName LIKE %:searchCharacter% " +
        "OR CAST(confirmPurchaseInvoiceEntity.purchaseInvoice AS string) LIKE %:searchCharacter% " +
        "OR FUNCTION('DATE_FORMAT', confirmPurchaseInvoiceEntity.purchaseDate, '%d-%m-%Y')LIKE %:searchCharacter% ")
    List<ConfirmPurchaseEntity> searchByVendorNameOrPurchaseInvoiceOrPurchaseDate(@PathVariable("searchCharacter") String searchCharacter);

    List<ConfirmPurchaseEntity> findByPurchaseDateBetween(LocalDateTime start, LocalDateTime end);

    List<ConfirmPurchaseEntity> findByVendorEntity_VendorIdAndPurchaseDateBetweenOrderByConfirmPurchaseId(Long vendorId,LocalDateTime start, LocalDateTime end);


}
