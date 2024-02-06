package com.appli.clcapi.tempInvoice.serviceImple;


import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.customer.repository.CustomerRepo;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import com.appli.clcapi.tempInvoice.service.TempInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class TempInvoiceServiceImple implements TempInvoiceService {

    private final CustomerRepo customerRepo;
    private final TempInvoiceRepo tempInvoiceRepo;
    @Override
    public ResponseEntity<String> register(TempInvoiceDto tempInvoiceDto) {
        try {
            Long custId = tempInvoiceDto.getCustomerOBJ().getCustId();
            Optional<CustomerEntity> aCustomer =  customerRepo.findById(custId);
            if(aCustomer.isPresent()){
                CustomerEntity aCustomerGet = aCustomer.get();
                var anInvoice = TempInvoiceEntity.builder()
                        .tempInvoiceId(tempInvoiceDto.getTempInvoiceId())
                        .date(tempInvoiceDto.getDate())
                        .netAmount(tempInvoiceDto.getNetAmount())
                        .customer(new CustomerEntity(
                                aCustomerGet.getCustId(),
                                aCustomerGet.getCustName(),
                                aCustomerGet.getContact(),
                                aCustomerGet.getAddress(),
                                aCustomerGet.getEmail()
                        ))
                        .build();
                tempInvoiceRepo.save(anInvoice);
            }else{
                return new ResponseEntity<>("select an existing user", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("Invoice has been created",HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Server Error",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<String> delete(Long tempInvoiceId) {
     tempInvoiceRepo.deleteById(tempInvoiceId);
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> update(TempInvoiceDto tempInvoiceDto) {
        Optional<TempInvoiceEntity> aTempInvoice = tempInvoiceRepo.findById(tempInvoiceDto.getTempInvoiceId());
        Long custId = tempInvoiceDto.getCustomerOBJ().getCustId();
        Optional<CustomerEntity> selectedCustomer =  customerRepo.findById(custId);
        CustomerEntity aCustomerGet ;
        TempInvoiceEntity updatedTempInvoice;
        if(selectedCustomer.isPresent()){
            updatedTempInvoice =  aTempInvoice.get();
            aCustomerGet =  selectedCustomer.get();
            updatedTempInvoice.setDate(tempInvoiceDto.getDate());
            updatedTempInvoice.setNetAmount(tempInvoiceDto.getNetAmount());
            updatedTempInvoice.setCustomer(
                    new CustomerEntity(
                            aCustomerGet.getCustId(),
                            aCustomerGet.getCustName(),
                            aCustomerGet.getContact(),
                            aCustomerGet.getAddress(),
                            aCustomerGet.getEmail()
                    )
            );
            tempInvoiceRepo.save(updatedTempInvoice);
        }else{
            return new ResponseEntity<>("Select an existing customer",HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Invoice has been updated",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<?>> getAll() {
        List<TempInvoiceEntity> existingTempInvoices = tempInvoiceRepo.findAll();
        List<TempInvoiceDto> listOfTempInvoiceDto = new ArrayList<>();
        for(TempInvoiceEntity aTempInvoice: existingTempInvoices){
            TempInvoiceDto tempInvoiceDto =  new TempInvoiceDto(aTempInvoice);
            listOfTempInvoiceDto.add(tempInvoiceDto);
        }
        return new ResponseEntity<>(listOfTempInvoiceDto,HttpStatus.OK) ;
    }

    @Override
    public ResponseEntity<ArrayList<?>> select(String existingChar) {
        Iterable<TempInvoiceEntity> searchedListOfTempInvoices;
        searchedListOfTempInvoices = tempInvoiceRepo.findByCustomerCustNameContainingIgnoreCase(existingChar);
        ArrayList<TempInvoiceDto> listOfDtoForView = new ArrayList<>();
        for(TempInvoiceEntity anInvoice : searchedListOfTempInvoices){
            TempInvoiceDto anInvoiceDto = new TempInvoiceDto(anInvoice);
            listOfDtoForView.add(anInvoiceDto);
        }
        return new ResponseEntity<>(listOfDtoForView,HttpStatus.OK);
    }
}
