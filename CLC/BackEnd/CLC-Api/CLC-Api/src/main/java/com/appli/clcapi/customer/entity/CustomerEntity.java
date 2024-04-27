package com.appli.clcapi.customer.entity;



import com.appli.clcapi.customer.dto.CustomerDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
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
    private long contact;
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TempInvoiceEntity> tempInvoiceEntity;

    private boolean deleted = false;


    public CustomerEntity(Long custId){
        this.custId = custId;
    }

    public CustomerEntity(CustomerDto customerDto) {
        this.custId = customerDto.getCustId();
        this.custName = customerDto.getCustName();
        this.address = customerDto.getAddress();
        this.contact = customerDto.getContact();
        this.email = customerDto.getEmail();
        this.deleted = customerDto.isDeleted();
    }



}
