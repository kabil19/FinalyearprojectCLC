package com.appli.clcapi.customer.repository;

import com.appli.clcapi.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity, Long> {


    Iterable<CustomerEntity> findByEmailContainingIgnoreCaseOrAddressContainingIgnoreCaseOrCustNameContaining(String email, String address,  String custName);
}
