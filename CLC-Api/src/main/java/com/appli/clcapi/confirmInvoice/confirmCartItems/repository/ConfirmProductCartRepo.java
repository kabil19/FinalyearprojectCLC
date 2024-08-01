package com.appli.clcapi.confirmInvoice.confirmCartItems.repository;

import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConfirmProductCartRepo extends JpaRepository<ConfirmProductCartEntity, Long> {


    List<ConfirmProductCartEntity> findByConfirmInvoiceEntity_ConfirmInvoiceId(long invoiceId);
}
