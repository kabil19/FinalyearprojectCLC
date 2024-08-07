package com.appli.clcapi.confirmInvoice.dto;

import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
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

    public ConfirmInvoiceDto(ConfirmSalesInvoiceEntity confirmSalesInvoiceEntity) {
        this.confirmInvoiceId = confirmSalesInvoiceEntity.getConfirmInvoiceId();
        this.date = confirmSalesInvoiceEntity.getDate();
        this.netAmount = confirmSalesInvoiceEntity.getNetAmount();
        this.paidAmount = confirmSalesInvoiceEntity.getPaidAmount();
        this.customerOBJ = (new CustomerDto(confirmSalesInvoiceEntity.getCustomer()));
        this.invoiceReference = confirmSalesInvoiceEntity.getInvoiceReference();
        this.isComplete = confirmSalesInvoiceEntity.getIsComplete();
        this.advanceAmount = confirmSalesInvoiceEntity.getAdvancePayment();
    }
}
