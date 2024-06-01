package com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository;

import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCashEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmCashRepo extends JpaRepository<ConfirmCashEntity, Long> {

}
