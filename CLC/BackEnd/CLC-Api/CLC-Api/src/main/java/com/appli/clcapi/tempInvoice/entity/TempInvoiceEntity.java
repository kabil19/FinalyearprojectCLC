package com.appli.clcapi.tempInvoice.entity;
import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.payments.entity.PaymentsEntity;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;


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

    private Long tempInvoiceNumber;

    private Date date;

    private Double netAmount;

    private Boolean finalized = false;

    private Double paidAmount;

    @OneToMany(mappedBy = "tempInvoiceEntity" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCartEntity> productCartEntity;

    @OneToMany(mappedBy = "tempInvoiceEntity" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmProductCartEntity> confirmProductCartEntity;
    @ManyToOne
    @JoinColumn(name="custId", nullable = false)
    private CustomerEntity customer;


    @OneToMany(mappedBy = "salesInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentsEntity> paymentsEntities;
    public TempInvoiceEntity(TempInvoiceDto tempInvoiceDto){
        super();
        this.setTempInvoiceNumber(tempInvoiceDto.getTempInvoiceNumber());
        this.setTempInvoiceId(tempInvoiceDto.getTempInvoiceId());
        this.setDate(tempInvoiceDto.getDate());
        this.setNetAmount(tempInvoiceDto.getNetAmount());
        this.setPaidAmount(tempInvoiceDto.getPaidAmount());
        this.setCustomer(new CustomerEntity(tempInvoiceDto.getCustomerOBJ()));
    }


}
