package com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository;

import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmChequeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmChequeRepo extends JpaRepository<ConfirmChequeEntity,Long> {


}
