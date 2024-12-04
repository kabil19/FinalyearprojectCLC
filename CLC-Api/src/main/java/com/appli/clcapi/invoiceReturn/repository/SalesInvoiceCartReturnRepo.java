package com.appli.clcapi.invoiceReturn.repository;

import com.appli.clcapi.invoiceReturn.entity.SalesInvoiceCartReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesInvoiceCartReturnRepo extends JpaRepository<SalesInvoiceCartReturnEntity,Long> {
    List<SalesInvoiceCartReturnEntity> findAllBySalesReturnInvoice_SalesReturnInvoiceId(long salesRetInvoiceId);

}
