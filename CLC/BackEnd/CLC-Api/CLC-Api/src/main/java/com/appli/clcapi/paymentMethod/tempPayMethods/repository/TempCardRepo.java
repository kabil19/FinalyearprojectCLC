package com.appli.clcapi.paymentMethod.tempPayMethods.repository;

import com.appli.clcapi.paymentMethod.tempPayMethods.entity.TempCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TempCardRepo extends JpaRepository<TempCardEntity,Long> {
    List<TempCardEntity> findByTempInvoiceEntity_TempInvoiceId(Long invoiceId);

}
