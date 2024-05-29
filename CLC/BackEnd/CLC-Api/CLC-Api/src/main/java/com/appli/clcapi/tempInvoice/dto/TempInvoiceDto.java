package com.appli.clcapi.tempInvoice.dto;


import com.appli.clcapi.customer.dto.CustomerDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempInvoiceDto {

    @JsonProperty("tempInvoiceId")
    private Long tempInvoiceId; // null

    @JsonProperty("date")
    private Date date; //24/01/2024

    @JsonProperty("netAmount")
    private Double netAmount; //10000

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("customerOBJ")
    private CustomerDto customerEntity; //{custId:1}

    @JsonProperty("tempInvoiceNumber")
    private Long tempInvoiceNumber;

    @JsonProperty("finalized")
    private Boolean finalized;
    //table get
    public TempInvoiceDto(TempInvoiceEntity tempInvoiceEntity){
        this.setFinalized(tempInvoiceEntity.getFinalized());
        this.setTempInvoiceId(tempInvoiceEntity.getTempInvoiceId());
        this.setDate(tempInvoiceEntity.getDate());
        this.setNetAmount(tempInvoiceEntity.getNetAmount());
        this.setPaidAmount(tempInvoiceEntity.getPaidAmount());
        this.setCustomerEntity(new CustomerDto(tempInvoiceEntity.getCustomer()));
        this.setTempInvoiceNumber(tempInvoiceEntity.getTempInvoiceNumber());
    }
}
