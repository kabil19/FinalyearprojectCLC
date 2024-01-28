package com.appli.clcapi.vendor.service;
import com.appli.clcapi.vendor.dto.VendorDto;

import java.util.ArrayList;
import java.util.List;

public interface VendorService {
    String register(VendorDto vendorDto);

    String delete(Long custId);

    String update(VendorDto vendorDto);

    List<VendorDto> getAll();

    ArrayList<VendorDto> selectVendors(String existingChar);
}
