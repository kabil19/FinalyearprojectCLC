package com.appli.clcapi.customer.serviceImple;

import com.appli.clcapi.customer.dto.CustomerDto;
import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.customer.repository.CustomerRepo;
import com.appli.clcapi.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImple implements CustomerService {
    private final CustomerRepo customerRepo;
    @Override
    public String register(CustomerDto customerDto) {
      var customer = CustomerEntity
              .builder()
                .custId(customerDto.getCustId())
//              id??
              .custName(customerDto.getCustName())
              .address(customerDto.getAddress())
              .email(customerDto.getEmail())
              .contact(customerDto.getContact())
              .build();
        customerRepo.save(customer);
       return "Customer Inserted";
    }

    @Override
    public String delete(Long custId) {
        try{
             var aCust = customerRepo.getReferenceById(custId);
             aCust.setDeleted(true);
             customerRepo.save(aCust);
             return "Customer Deleted";


        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Couldn't delete customer");
        }

    }

    @Override
    public String update(CustomerDto customerDto) {
        try{
            Optional<CustomerEntity> foundCust= customerRepo.findById(customerDto.getCustId());
            CustomerEntity updatedCustomer;
            if(foundCust.isPresent()){
                updatedCustomer = foundCust.get();
                updatedCustomer.setCustName(customerDto.getCustName());
                updatedCustomer.setEmail(customerDto.getEmail());
                updatedCustomer.setContact(customerDto.getContact());
                updatedCustomer.setAddress(customerDto.getAddress());
            }else{
                return "Couldn't find the customer";
            }
            customerRepo.save(updatedCustomer);
            return "Customer Updated";
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Error while updating the category");
        }

    }

    @Override
    public List<CustomerDto> getAll() {
        try{
            List<CustomerEntity> customersList = customerRepo.findAllByDeletedEquals(false);
            List<CustomerDto> customersForView = new ArrayList<>();
            for(CustomerEntity aCust : customersList){
                CustomerDto customerDto = new CustomerDto(aCust);
                customersForView.add(customerDto);
            }
            return customersForView;
        }catch (Exception e){
            e.printStackTrace();
           // throw new RuntimeException("");
        }

        return null;
    }

    @Override
    public ArrayList<CustomerDto> select(String existingChar) {


        Iterable<CustomerEntity> cust= customerRepo.findByEmailContainingIgnoreCaseOrAddressContainingIgnoreCaseOrCustNameContaining(
                existingChar,
                existingChar,
                existingChar
        );
        ArrayList<CustomerDto> customerForView = new ArrayList<>();
        for (CustomerEntity aCust : cust){
            if(!aCust.isDeleted()){
                CustomerDto customerDto = new CustomerDto(aCust);
                customerForView.add(customerDto);
            }

        }
        return customerForView;
    }
}
