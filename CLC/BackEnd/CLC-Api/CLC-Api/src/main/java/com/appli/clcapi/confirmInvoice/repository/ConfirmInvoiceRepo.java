package com.appli.clcapi.confirmInvoice.repository;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

@Repository
public interface ConfirmInvoiceRepo extends JpaRepository<ConfirmInvoiceEntity, Long> {
    @Query("SELECT cie FROM ConfirmInvoiceEntity cie " +
            "JOIN cie.customer ce " +
            "WHERE ce.custName LIKE %:searchCharacter% " +
            "OR CAST(cie.invoiceNumber AS string) LIKE %:searchCharacter% " +
            "OR FUNCTION('DATE_FORMAT', cie.date, '%Y-%m-%d %H:%i:%s')LIKE %:searchCharacter% ")
    List<ConfirmInvoiceEntity> searchByCustomerNameOrInvoiceNoOrInvoiceDate(@PathVariable("searchCharacter") String searchCharacter);

    ConfirmInvoiceEntity findByInvoiceNumber(long invoiceNo);

    List<ConfirmInvoiceEntity> findByDateBetween(Date start, Date end);
}
