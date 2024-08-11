package com.appli.clcapi.vendor.serviceImple;

import com.appli.clcapi.vendor.dto.VendorDto;
import com.appli.clcapi.vendor.entity.VendorEntity;
import com.appli.clcapi.vendor.repository.VendorRepo;
import com.appli.clcapi.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorRepo vendorRepo;
    @Override
    public ResponseEntity<String> register(VendorDto vendorDto) {
        try{
            if(vendorDto.getVendorName().isEmpty()){
                return new ResponseEntity<>("Name can't be empty", HttpStatus.BAD_REQUEST);
            } else if (vendorDto.getAddress().isEmpty()) {
                return new ResponseEntity<>("Address can't be empty", HttpStatus.BAD_REQUEST);
            } else if (vendorDto.getContact().isEmpty()) {
                return new ResponseEntity<>("Contact can't be empty", HttpStatus.BAD_REQUEST);
            }
            var vendor = VendorEntity.builder()
                    .vendorId(vendorDto.getVendorId())
                    .vendorName(vendorDto.getVendorName())
                    .email(vendorDto.getEmail())
                    .contact(vendorDto.getContact())
                    .address(vendorDto.getAddress())
                    .build();
            vendorRepo.save(vendor);
            return new ResponseEntity<>("Vendor is Created!", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<String> delete(Long vendorId) {
       try {
           VendorEntity aVendor = vendorRepo.getReferenceById(vendorId);
           if(aVendor.getTempPurchaseEntity().isEmpty()){
               aVendor.setDeleted(true);
               vendorRepo.save(aVendor);
               return new ResponseEntity<>("Vendor is Deleted!",HttpStatus.OK);
           }

       }catch (Exception e){
           e.printStackTrace();
           return new ResponseEntity<>("Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
       }
        return null;
    }

    @Override
    public ResponseEntity<String> update(VendorDto vendorDto) {
        try{
            if (vendorDto.getVendorName().isEmpty()) {
                return new ResponseEntity<>("Name can't be empty", HttpStatus.BAD_REQUEST);
            } else if (vendorDto.getAddress().isEmpty()) {
                return new ResponseEntity<>("Address can't be empty", HttpStatus.BAD_REQUEST);
            } else if (vendorDto.getContact().isEmpty()) {
                return new ResponseEntity<>("Contact can't be empty", HttpStatus.BAD_REQUEST);
            }
            Optional<VendorEntity> existingVendor = vendorRepo.findById(vendorDto.getVendorId());
            if (existingVendor.isPresent()) {
                VendorEntity aVendor = existingVendor.get();
                aVendor.setVendorName(vendorDto.getVendorName());
                aVendor.setEmail(vendorDto.getEmail());
                aVendor.setContact(vendorDto.getContact());
                aVendor.setAddress(vendorDto.getAddress());
                vendorRepo.save(aVendor);
            }
            return new ResponseEntity<>("Vendor is updated!", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
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
