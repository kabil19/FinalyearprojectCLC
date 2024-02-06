package com.appli.clcapi.customer.entity;



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


//    public CustomerEntity(CustomerDto customerDto) {
//        setCustId(customerDto.getCustId());
//        setCustName(customerDto.getCustName());
//        setContact(customerDto.getContact());
//        setAddress(customerDto.getAddress());
//        setEmail(customerDto.getEmail());
//    }


    public CustomerEntity(Long custId, String custName, long contact, String address, String email) {
        this.custId = custId;
        this.custName = custName;
        this.contact = contact;
        this.address = address;
        this.email = email;
    }
}
