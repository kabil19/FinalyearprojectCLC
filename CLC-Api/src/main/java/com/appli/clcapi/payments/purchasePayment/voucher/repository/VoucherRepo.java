package com.appli.clcapi.payments.purchasePayment.voucher.repository;

import com.appli.clcapi.payments.purchasePayment.voucher.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoucherRepo extends JpaRepository<VoucherEntity, Long> {

    List<VoucherEntity> findByConfirmPurchaseEntity_ConfirmPurchaseId(long purchaseId);
}
