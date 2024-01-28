package com.appli.clcapi.customer.repository;

import com.appli.clcapi.customer.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity, Long> {


    Iterable<CustomerEntity> findByEmailContainingIgnoreCaseOrAddressContainingIgnoreCaseOrCustNameContaining(String email, String address,  String custName);

    List<CustomerEntity> findAllByDeletedEquals(boolean state);
}
