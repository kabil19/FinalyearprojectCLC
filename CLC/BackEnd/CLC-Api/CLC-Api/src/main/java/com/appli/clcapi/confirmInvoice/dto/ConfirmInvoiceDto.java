package com.appli.clcapi.confirmInvoice.dto;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.customer.dto.CustomerDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmInvoiceDto {

    @JsonProperty("confirmInvoiceId")
    private Long confirmInvoiceId ;

    @JsonProperty("date")
    private Date date;

    @JsonProperty("netAmount")
    private Double netAmount;

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("customerOBJ")
    private CustomerDto customerOBJ;

    @JsonProperty("invoiceNumber")
    private Long invoiceNumber;

    public ConfirmInvoiceDto(ConfirmInvoiceEntity confirmInvoiceEntity) {
        this.confirmInvoiceId = confirmInvoiceEntity.getConfirmInvoiceId();
        this.date = confirmInvoiceEntity.getDate();
        this.netAmount = confirmInvoiceEntity.getNetAmount();
        this.paidAmount = confirmInvoiceEntity.getPaidAmount();
        this.customerOBJ = (new CustomerDto(confirmInvoiceEntity.getCustomer()));
        this.invoiceNumber = confirmInvoiceEntity.getInvoiceNumber();
    }
}
