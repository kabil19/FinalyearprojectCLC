package com.appli.clcapi.tempInvoice.entity;
import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.payments.invoicePayments.tempPayments.entity.TempPaymentsEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempChequeEntity;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import jakarta.persistence.*;
import lombok.*;


import java.time.LocalDateTime;

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

    private LocalDateTime date;

    private Double netAmount;

    private Boolean finalized = false;

    private Double paidAmount;

    @OneToMany(mappedBy = "tempInvoiceEntity" ,cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCartEntity> productCartEntity;

    private Boolean isComplete = false;


    @ManyToOne
    @JoinColumn(name="custId", nullable = false)
    private CustomerEntity customer;

    @OneToMany(mappedBy = "tempSalesInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TempPaymentsEntity> tempPaymentsEntities;

    @OneToMany(mappedBy = "tempInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TempCardEntity> tempCardEntity;//checked

    @OneToMany(mappedBy = "tempInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TempCashEntity> tempCashEntity;//checked

    @OneToMany(mappedBy = "tempInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TempChequeEntity> tempChequeEntity;//checked

    public TempInvoiceEntity(TempInvoiceDto tempInvoiceDto){
        super();
        this.setTempInvoiceNumber(tempInvoiceDto.getTempInvoiceNumber());
        this.setTempInvoiceId(tempInvoiceDto.getTempInvoiceId());
        this.setDate(tempInvoiceDto.getDate());
        this.setNetAmount(tempInvoiceDto.getNetAmount());
        this.setPaidAmount(tempInvoiceDto.getPaidAmount());
        this.setCustomer(new CustomerEntity(tempInvoiceDto.getCustomerEntity()));
       this.setIsComplete(tempInvoiceDto.getIsComplete());
    }


}
