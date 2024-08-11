package com.appli.clcapi.customer.service;

import com.appli.clcapi.common.response.NonPaginatedResponse;
import com.appli.clcapi.customer.dto.CustomerDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface CustomerService {
    NonPaginatedResponse register(CustomerDto customerDto);

    ResponseEntity<String> delete(Long custId);

    ResponseEntity<String>  update(CustomerDto customerDto);

    List<CustomerDto> getAll();

    ArrayList<CustomerDto> select(String existingChar);
}
