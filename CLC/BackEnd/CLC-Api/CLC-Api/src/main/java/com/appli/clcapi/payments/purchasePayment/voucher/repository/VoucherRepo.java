package com.appli.clcapi.payments.purchasePayment.voucher.repository;

import com.appli.clcapi.payments.purchasePayment.voucher.entity.VoucherEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepo extends JpaRepository<VoucherEntity, Long> {

}
