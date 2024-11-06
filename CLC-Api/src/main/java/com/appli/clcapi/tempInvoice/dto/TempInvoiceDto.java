package com.appli.clcapi.tempInvoice.dto;


import com.appli.clcapi.customer.dto.CustomerDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempInvoiceDto {

    @JsonProperty("tempInvoiceId")
    private Long tempInvoiceId; // null

    @JsonProperty("date")
    private LocalDateTime date; //24/01/2024

    @JsonProperty("netAmount")
    private Double netAmount; //10000

     @JsonProperty("mainDiscount")
    private Double mainDiscount;

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("customerOBJ")
    private CustomerDto customerEntity; //{custId:1}

    @JsonProperty("tempInvoiceNumberRef")
    private String tempInvoiceNumberReference;

    @JsonProperty("finalized")
    private Boolean finalized;

    @JsonProperty("isComplete")
    private Boolean isComplete;
    //table get
    public TempInvoiceDto(TempInvoiceEntity tempInvoiceEntity){
        this.setFinalized(tempInvoiceEntity.getFinalized());
        this.setTempInvoiceId(tempInvoiceEntity.getTempInvoiceId());
        this.setDate(tempInvoiceEntity.getDate());
        this.setNetAmount(tempInvoiceEntity.getNetAmount());
        this.setPaidAmount(tempInvoiceEntity.getPaidAmount());
        this.setCustomerEntity(new CustomerDto(tempInvoiceEntity.getCustomer()));
        this.setTempInvoiceNumberReference(tempInvoiceEntity.getTempInvoiceNumberReference());
        this.setIsComplete(tempInvoiceEntity.getIsComplete());
        this.setMainDiscount(tempInvoiceEntity.getMainDiscount());
    }


}
