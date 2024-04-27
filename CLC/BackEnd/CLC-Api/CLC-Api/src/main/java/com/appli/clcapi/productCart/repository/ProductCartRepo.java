package com.appli.clcapi.productCart.repository;


import com.appli.clcapi.productCart.entity.ProductCartEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCartRepo extends JpaRepository<ProductCartEntity, Long> {

List<ProductCartEntity> findByTempInvoiceEntity_TempInvoiceId(Long invoiceID);
    Optional<ProductCartEntity> findByStockEntity_StockIdAndTempInvoiceEntity_TempInvoiceId(Long stockId, Long tempInvoiceId);



}
