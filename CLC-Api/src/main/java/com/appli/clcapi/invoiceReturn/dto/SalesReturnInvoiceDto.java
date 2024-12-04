package com.appli.clcapi.invoiceReturn.dto;

import com.appli.clcapi.invoiceReturn.entity.SalesReturnInvoice;
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
public class SalesReturnInvoiceDto {

    @JsonProperty("confirmInvoiceId")
    private Long salesReturnInvoiceId ;

    @JsonProperty("date")
    private LocalDateTime date;

    @JsonProperty("netAmount")
    private Double netAmount;

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("advanceAmount")
    private Double advanceAmount;

    @JsonProperty("mainDiscount")
    private Double mainDiscount;

    @JsonProperty("returnAmount")
    private Double returnAmount;


    @JsonProperty("invoiceNumberRef")
    private String invoiceReference;

    public SalesReturnInvoiceDto(SalesReturnInvoice salesReturnInvoice) {
        this.salesReturnInvoiceId = salesReturnInvoice.getSalesReturnInvoiceId();
        this.date = salesReturnInvoice.getDate();
        this.netAmount = salesReturnInvoice.getNetAmount();
        this.paidAmount = salesReturnInvoice.getPaidAmount();
        this.invoiceReference = salesReturnInvoice.getInvoiceReference();
        this.advanceAmount = salesReturnInvoice.getAdvancePayment();
        this.mainDiscount = salesReturnInvoice.getMainDiscount();
        this.returnAmount = salesReturnInvoice.getReturnAmount();
    }
}
