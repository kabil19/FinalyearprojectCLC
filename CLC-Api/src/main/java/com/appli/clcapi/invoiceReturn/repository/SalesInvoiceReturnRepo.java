package com.appli.clcapi.invoiceReturn.repository;

import com.appli.clcapi.invoiceReturn.entity.SalesReturnInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesInvoiceReturnRepo extends JpaRepository<SalesReturnInvoice,Long> {
}
