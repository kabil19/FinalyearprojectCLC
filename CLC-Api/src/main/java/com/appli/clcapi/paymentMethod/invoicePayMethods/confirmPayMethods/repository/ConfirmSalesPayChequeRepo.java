package com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository;

import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmSalesInvoiceChequeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmSalesPayChequeRepo extends JpaRepository<ConfirmSalesInvoiceChequeEntity,Long> {


}
