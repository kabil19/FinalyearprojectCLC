package com.appli.clcapi.payments.invoicePayments.confirmPayments.dto;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfirmPaymentsDto {
    @JsonProperty("paymentId")
    private Long paymentId;
    @JsonProperty("paymentType")
    private String paymentType;
    @JsonProperty("paidDate")
    private Date paidDate;
    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("confirmInvoiceDto")
    private ConfirmInvoiceDto confirmInvoiceDto;
    @JsonProperty("chequeRefNo")
    private Long chequeRefNo;
    @JsonProperty("chequeDueDate")
    private Date chequeDueDate;
    @JsonProperty("cardRefNo")
    private Long cardRefNo;
//    private Long purchaseInvoice;


}
