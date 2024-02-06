package com.appli.clcapi.customer.service;

import com.appli.clcapi.customer.dto.CustomerDto;

import java.util.ArrayList;
import java.util.List;

public interface CustomerService {
    String register(CustomerDto customerDto);

    String delete(Long custId);

    String update(CustomerDto customerDto);

    List<CustomerDto> getAll();

    ArrayList<CustomerDto> select(String existingChar);
}
