package com.appli.clcapi.confirmInvoice.confirmCartItems.repository;

import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmProductCartRepo extends JpaRepository<ConfirmProductCartEntity, Long> {
}
