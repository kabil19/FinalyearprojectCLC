package com.appli.clcapi.vendor.serviceImple;

import com.appli.clcapi.vendor.dto.VendorDto;
import com.appli.clcapi.vendor.entity.VendorEntity;
import com.appli.clcapi.vendor.repository.VendorRepo;
import com.appli.clcapi.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorServiceImple implements VendorService {
    private final VendorRepo vendorRepo;
    @Override
    public String register(VendorDto vendorDto) {
        try{
            var vendor = VendorEntity.builder()
                    .vendorId(vendorDto.getVendorId())
                    .vendorName(vendorDto.getVendorName())
                    .email(vendorDto.getEmail())
                    .contact(vendorDto.getContact())
                    .address(vendorDto.getAddress())
                    .build();
            vendorRepo.save(vendor);
            return "Vendor Inserted";
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException();
        }

    }

    @Override
    public String delete(Long vendorId) {
       try {
           VendorEntity aVendor = vendorRepo.getReferenceById(vendorId);
           aVendor.setDeleted(true);
           vendorRepo.save(aVendor);
           return "Vendor Has been Deleted";
       }catch (Exception e){
           e.printStackTrace();
           throw new RuntimeException();
       }
    }

    @Override
    public String update(VendorDto vendorDto) {
        Optional<VendorEntity> existingVendor = vendorRepo.findById(vendorDto.getVendorId());
        if(existingVendor.isPresent()){
            VendorEntity aVendor = existingVendor.get();
            aVendor.setVendorName(vendorDto.getVendorName());
            aVendor.setEmail(vendorDto.getEmail());
            aVendor.setContact(vendorDto.getContact());
            aVendor.setAddress(vendorDto.getAddress());
            vendorRepo.save(aVendor);
        }
        return "Vendor has been Updated";
    }

    @Override
    public List<VendorDto> getAll() {
        List<VendorEntity> vendors = vendorRepo.findAllByDeletedEquals(false);
        List<VendorDto> vendorDtoList = new ArrayList<>();
        for (VendorEntity aVendor : vendors){
           VendorDto newVendor =  new VendorDto(aVendor);
           vendorDtoList.add(newVendor);
        }
        return vendorDtoList;
    }

    @Override
    public ArrayList<VendorDto> selectVendors(String existingChar) {
        Iterable<VendorEntity> vendorList = vendorRepo.findByEmailIsContainingIgnoreCaseOrAddressContainingIgnoreCaseOrVendorNameContainsIgnoreCase(
                existingChar,
                existingChar,
                existingChar
        );

        ArrayList<VendorDto> vendorDtoList = new ArrayList<>();
        for(VendorEntity aVendor: vendorList){
            if(!aVendor.isDeleted()){
                VendorDto vendorDto = new VendorDto(aVendor);
                vendorDtoList.add(vendorDto);
            }
        }
        return vendorDtoList;
    }
}
