package com.appli.clcapi.vendor.repository;

import com.appli.clcapi.vendor.entity.VendorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepo extends JpaRepository<VendorEntity, Long> {

    Iterable<VendorEntity> findByEmailIsContainingIgnoreCaseOrAddressContainingIgnoreCaseOrVendorNameContainsIgnoreCase
            (String email, String address, String vendorName);

    List<VendorEntity> findAllByDeletedEquals(boolean state);

}
