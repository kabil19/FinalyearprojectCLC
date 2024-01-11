package com.appli.clcapi.customer.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cust_tbl")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long custId;
    private String custName;
    private String address;
    private Integer contact;
    private String email;

//    CustomerEntity(CustomerDto customerDto){
//        this.custId = customerDto.getCustId();
//        this.custName = customerDto.getCustName();
//        this.address = customerDto.getAddress();
//        this.email = customerDto.getEmail();
//        this.contact = customerDto.getContact();
//    }
}
