package com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.repository;

import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmCardRepo extends JpaRepository<ConfirmCardEntity,Long> {

}
