package com.appli.clcapi.customer.controller;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.customer.dto.CustomerDto;
import com.appli.clcapi.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/customer/")
@CrossOrigin("http://localhost:4200")
public class CustomerController {

    private final CustomerService customerService;
    @PostMapping("register")
    public NonPaginatedResponse register(@RequestBody CustomerDto customerDto){
        return customerService.register(customerDto);
    }

    @DeleteMapping("delete/{custId}")
    public ResponseEntity<String> delete(@PathVariable Long custId){
        return customerService.delete(custId);
    }

    @PutMapping("update")
    public ResponseEntity<String>  update(@RequestBody  CustomerDto customerDto){
        return customerService.update(customerDto);
    }

    @GetMapping("getAll")
    public List<CustomerDto> getAll(){
        return customerService.getAll();
    }

    @GetMapping("select/{existingChar}")
    public ArrayList<CustomerDto> select(@PathVariable String existingChar){
        return customerService.select(existingChar);
    }

}
