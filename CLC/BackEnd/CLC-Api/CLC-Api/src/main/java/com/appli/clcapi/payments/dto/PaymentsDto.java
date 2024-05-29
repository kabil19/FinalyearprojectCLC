package com.appli.clcapi.payments.dto;

import com.appli.clcapi.payments.entity.PaymentsEntity;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentsDto {
    @JsonProperty("paymentId")
    private Long paymentId;
    @JsonProperty("paymentType")
    private String paymentType;
    @JsonProperty("paidDate")
    private Date paidDate;
    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("salesInvoice")
    private TempInvoiceDto salesInvoice;
    @JsonProperty("chequeRefNo")
    private Long chequeRefNo;
    @JsonProperty("chequeDueDate")
    private Date chequeDueDate;
    @JsonProperty("cardRefNo")
    private Long cardRefNo;
//    private Long purchaseInvoice;

    public PaymentsDto(PaymentsEntity paymentsEntity){
        setPaymentId(paymentsEntity.getPaymentId());
        setPaymentType(paymentsEntity.getPaymentType());
        setPaidAmount(paymentsEntity.getPaidAmount());
        setPaidDate(paymentsEntity.getPaidDate());
        setSalesInvoice(new TempInvoiceDto(paymentsEntity.getSalesInvoice()));
    }
}
