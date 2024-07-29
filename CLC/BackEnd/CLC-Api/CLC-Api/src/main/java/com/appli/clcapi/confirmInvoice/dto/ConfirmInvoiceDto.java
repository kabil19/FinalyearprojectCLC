package com.appli.clcapi.confirmInvoice.dto;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.customer.dto.CustomerDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmInvoiceDto {

    @JsonProperty("confirmInvoiceId")
    private Long confirmInvoiceId ;

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("netAmount")
    private Double netAmount;

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("advanceAmount")
    private Double advanceAmount;


    @JsonProperty("isComplete")
    private Boolean isComplete;

    @JsonProperty("customerOBJ")
    private CustomerDto customerOBJ;

    @JsonProperty("invoiceNumberRef")
    private String invoiceReference;

    public ConfirmInvoiceDto(ConfirmInvoiceEntity confirmInvoiceEntity) {
        this.confirmInvoiceId = confirmInvoiceEntity.getConfirmInvoiceId();
        this.date = confirmInvoiceEntity.getDate();
        this.netAmount = confirmInvoiceEntity.getNetAmount();
        this.paidAmount = confirmInvoiceEntity.getPaidAmount();
        this.customerOBJ = (new CustomerDto(confirmInvoiceEntity.getCustomer()));
        this.invoiceReference = confirmInvoiceEntity.getInvoiceReference();
        this.isComplete = confirmInvoiceEntity.getIsComplete();
        this.advanceAmount = confirmInvoiceEntity.getAdvancePayment();
    }
}
