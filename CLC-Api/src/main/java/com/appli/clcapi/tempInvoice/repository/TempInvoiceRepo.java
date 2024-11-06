package com.appli.clcapi.tempInvoice.repository;

import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface TempInvoiceRepo extends JpaRepository<TempInvoiceEntity, Long> {


    Iterable<TempInvoiceEntity> findByCustomerCustNameContainingIgnoreCase(String custName);


//     TempInvoiceEntity findAllByCustomer_CustIdAndCustomer_Deleted(Long custId, Boolean deleted);

}
