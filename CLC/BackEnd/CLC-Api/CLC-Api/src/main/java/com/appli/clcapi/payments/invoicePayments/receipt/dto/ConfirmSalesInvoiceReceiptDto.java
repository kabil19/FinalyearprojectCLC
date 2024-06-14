package com.appli.clcapi.payments.invoicePayments.receipt.dto;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.payments.invoicePayments.receipt.entity.ConfirmSalesInvoiceReceiptEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmSalesInvoiceReceiptDto {

    @JsonProperty("receiptId")
    private Long receiptId;
    @JsonProperty("confirmInvoiceOBJ")
    private ConfirmInvoiceDto confirmInvoiceDto;
    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("paidDate")
    private Date paidDate;
    @JsonProperty("paymentType")
    private String paymentType;



    public ConfirmSalesInvoiceReceiptDto(ConfirmSalesInvoiceReceiptEntity confirmSalesInvoiceReceiptEntity){
        this.setReceiptId(confirmSalesInvoiceReceiptEntity.getReceiptId());
        this.setConfirmInvoiceDto(new ConfirmInvoiceDto(confirmSalesInvoiceReceiptEntity.getConfirmInvoiceEntity()));
        this.setPaidAmount(confirmSalesInvoiceReceiptEntity.getPaidAmount());
        this.setPaidDate(confirmSalesInvoiceReceiptEntity.getPaidDate());
        this.setPaymentType(confirmSalesInvoiceReceiptEntity.getPaymentType());

    }
}
