package com.appli.clcapi.confirmInvoice.repository;

import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConfirmSalesInvoiceRepo extends JpaRepository<ConfirmSalesInvoiceEntity, Long> {
    @Query("SELECT confirmSalesInvoiceEntity FROM ConfirmSalesInvoiceEntity confirmSalesInvoiceEntity " +
            "JOIN confirmSalesInvoiceEntity.customer custEntity " +
            "WHERE custEntity.custName LIKE %:searchCharacter% " +
            "OR CAST(confirmSalesInvoiceEntity.invoiceReference AS string) LIKE %:searchCharacter% " +
            "OR FUNCTION('DATE_FORMAT', confirmSalesInvoiceEntity.date, '%d-%m-%Y')LIKE %:searchCharacter% ")
    List<ConfirmSalesInvoiceEntity> searchByCustomerNameOrInvoiceNoOrInvoiceDate(@PathVariable("searchCharacter") String searchCharacter);

    ConfirmSalesInvoiceEntity findByInvoiceReference(String invoiceNo);

    List<ConfirmSalesInvoiceEntity> findByDateBetween(LocalDateTime start, LocalDateTime end);


    List<ConfirmSalesInvoiceEntity> findByCustomer_CustIdAndDateBetweenOrderByConfirmInvoiceId(Long custId, LocalDateTime start, LocalDateTime end);

}
