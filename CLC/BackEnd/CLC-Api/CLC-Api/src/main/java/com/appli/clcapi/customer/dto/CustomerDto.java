package com.appli.clcapi.customer.dto;

import com.appli.clcapi.customer.entity.CustomerEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDto {

    @JsonProperty("custId")
    private Long custId; //1

    @JsonProperty("custName")
    private String custName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("address")
    private String address;

    @JsonProperty("contact")
    private long contact;

    @JsonProperty(value = "deleted")
    @JsonIgnore
    private boolean deleted;
    public CustomerDto(CustomerEntity customerEntity){
        this.custId = customerEntity.getCustId();
        this.email = customerEntity.getEmail();
        this.address = customerEntity.getAddress();
        this.contact = customerEntity.getContact();
        this.custName = customerEntity.getCustName();
    }




    public CustomerDto(String custName) {
        setCustName(custName);
    }
}
