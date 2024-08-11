package com.appli.clcapi.vendor.service;
import com.appli.clcapi.vendor.dto.VendorDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface VendorService {
    ResponseEntity<String> register(VendorDto vendorDto);

    ResponseEntity<String> delete(Long custId);

    ResponseEntity<String> update(VendorDto vendorDto);

    List<VendorDto> getAll();

    ArrayList<VendorDto> selectVendors(String existingChar);
}
