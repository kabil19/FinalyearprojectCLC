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
    private Long netAmount; //10000

    @JsonProperty("customerOBJ")
    private CustomerDto customerOBJ; //{custId:1}


    //table get
    public TempInvoiceDto(TempInvoiceEntity tempInvoiceEntity){
        this.setTempInvoiceId(tempInvoiceEntity.getTempInvoiceId());
        this.setDate(tempInvoiceEntity.getDate());
        this.setNetAmount(tempInvoiceEntity.getNetAmount());
        this.setCustomerOBJ(new CustomerDto(tempInvoiceEntity.getCustomer()));
       // this.setCustomerOBJ(new CustomerDto( tempInvoiceEntity.getCustomer().getCustName()));
    }
}
