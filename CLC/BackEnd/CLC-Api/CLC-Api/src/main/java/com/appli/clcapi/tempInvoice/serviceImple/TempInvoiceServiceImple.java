package com.appli.clcapi.tempInvoice.serviceImple;


import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.customer.repository.CustomerRepo;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.appli.clcapi.tempInvoice.repository.TempInvoiceRepo;
import com.appli.clcapi.tempInvoice.service.TempInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@RequiredArgsConstructor
@Service
public class TempInvoiceServiceImple implements TempInvoiceService {

    private final CustomerRepo customerRepo;
    private final TempInvoiceRepo tempInvoiceRepo;
    @Override
    public String register(TempInvoiceDto tempInvoiceDto) {
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
            return "select an existing user";
        }

        return "Invoice has been created";
    }

    @Override
    public String delete(Long tempInvoiceId) {
     tempInvoiceRepo.deleteById(tempInvoiceId);
        return "deleted";
    }

    @Override
    public String update(TempInvoiceDto tempInvoiceDto) {
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
            return "Select an existing customer";
        }

        return "Invoice has been updated";
    }

    @Override
    public List<TempInvoiceDto> getAll() {
        List<TempInvoiceEntity> existingTempInvoices = tempInvoiceRepo.findAll();
        List<TempInvoiceDto> listOfTempInvoiceDto = new ArrayList<>();
        for(TempInvoiceEntity aTempInvoice: existingTempInvoices){
            TempInvoiceDto tempInvoiceDto =  new TempInvoiceDto(aTempInvoice);
            listOfTempInvoiceDto.add(tempInvoiceDto);
        }
        return listOfTempInvoiceDto;
    }

    @Override
    public ArrayList<TempInvoiceDto> select(String existingChar) {
        Iterable<TempInvoiceEntity> searchedListOfTempInvoices;
        searchedListOfTempInvoices = tempInvoiceRepo.findByCustomerCustNameContainingIgnoreCase(existingChar);
        ArrayList<TempInvoiceDto> listOfDtoForView = new ArrayList<>();
        for(TempInvoiceEntity anInvoice : searchedListOfTempInvoices){
            TempInvoiceDto anInvoiceDto = new TempInvoiceDto(anInvoice);
            listOfDtoForView.add(anInvoiceDto);
        }
        return listOfDtoForView;
    }
}
