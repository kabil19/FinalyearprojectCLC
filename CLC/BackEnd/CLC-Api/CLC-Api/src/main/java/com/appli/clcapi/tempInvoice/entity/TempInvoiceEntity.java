package com.appli.clcapi.tempInvoice.entity;




import com.appli.clcapi.customer.entity.CustomerEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@Builder
@Entity(name = "tempInvoice")
@AllArgsConstructor
@NoArgsConstructor
public class TempInvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tempInvoiceId;

    private Date date;

    private Long netAmount;

    @ManyToOne
    @JoinColumn(name="custId", nullable = false)
    private CustomerEntity customer;

    TempInvoiceEntity(TempInvoiceEntity tempInvoiceEntity){
        super();
        this.setTempInvoiceId(tempInvoiceEntity.getTempInvoiceId());
        this.setDate(tempInvoiceEntity.getDate());
        this.setNetAmount(tempInvoiceEntity.getNetAmount());
        this.setCustomer(new CustomerEntity());
    }

}
