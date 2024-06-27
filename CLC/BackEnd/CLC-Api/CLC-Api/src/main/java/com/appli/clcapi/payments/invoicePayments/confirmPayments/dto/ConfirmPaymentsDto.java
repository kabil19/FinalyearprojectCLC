package com.appli.clcapi.payments.invoicePayments.confirmPayments.dto;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmPaymentsEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime paidDate;
    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("confirmInvoiceOBJ")
    private ConfirmInvoiceDto confirmInvoiceDto;
    @JsonProperty("chequeRefNo")
    private Long chequeRefNo;
    @JsonProperty("chequeDueDate")
    private LocalDateTime chequeDueDate;
    @JsonProperty("cardRefNo")
    private Long cardRefNo;

//    private Long purchaseInvoice;

    public ConfirmPaymentsDto(ConfirmPaymentsEntity confirmPaymentsEntity) {
        this.setPaymentId(confirmPaymentsEntity.getPaymentId());
        this.setConfirmInvoiceDto(new ConfirmInvoiceDto(confirmPaymentsEntity.getConfirmInvoice()));
        this.setPaymentType(confirmPaymentsEntity.getPaymentType());
        this.setPaidAmount(confirmPaymentsEntity.getPaidAmount());
        this.setPaidDate(confirmPaymentsEntity.getPaidDate());
    }

}
