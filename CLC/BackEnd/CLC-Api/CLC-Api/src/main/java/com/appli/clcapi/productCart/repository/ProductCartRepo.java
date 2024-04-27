package com.appli.clcapi.productCart.repository;


import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCartRepo extends JpaRepository<ProductCartEntity, Long> {
//    Optional<ProductCartEntity> findByStockEntityAndTempInvoiceEntity(StockEntity stockEntity, TempInvoiceEntity tempInvoiceEntity);
//    List<ProductCartEntity> findProductCartEntitiesByTempInvoiceEntity_TempInvoiceId(Long invoiceId);
List<ProductCartEntity> findByTempInvoiceEntity_TempInvoiceId(Long invoiceID);
    Optional<ProductCartEntity> findByStockEntity_StockIdAndTempInvoiceEntity_TempInvoiceId(Long stockId, Long tempInvoiceId);



}
