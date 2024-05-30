package com.appli.clcapi.paymentMethod.confirmPayMethods.repository;

import com.appli.clcapi.paymentMethod.confirmPayMethods.entity.ConfirmCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmCardRepo extends JpaRepository<ConfirmCardEntity,Long> {

}
