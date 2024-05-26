package com.appli.clcapi.confirmInvoice.repository;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmInvoiceRepo extends JpaRepository<ConfirmInvoiceEntity, Long> {
}
