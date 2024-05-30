package com.appli.clcapi.paymentMethod.confirmPayMethods.repository;

import com.appli.clcapi.paymentMethod.confirmPayMethods.entity.ConfirmChequeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmChequeRepo extends JpaRepository<ConfirmChequeEntity,Long> {


}
