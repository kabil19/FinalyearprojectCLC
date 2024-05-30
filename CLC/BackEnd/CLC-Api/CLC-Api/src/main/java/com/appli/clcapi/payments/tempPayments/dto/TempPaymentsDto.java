package com.appli.clcapi.payments.tempPayments.dto;

import com.appli.clcapi.payments.tempPayments.entity.TempPaymentsEntity;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempPaymentsDto {
    @JsonProperty("paymentId")
    private Long paymentId;
    @JsonProperty("paymentType")
    private String paymentType;
    @JsonProperty("paidDate")
    private Date paidDate;
    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("salesInvoice")
    private TempInvoiceDto tempSalesInvoice;
    @JsonProperty("chequeRefNo")
    private Long chequeRefNo;
    @JsonProperty("chequeDueDate")
    private Date chequeDueDate;
    @JsonProperty("cardRefNo")
    private Long cardRefNo;
//    private Long purchaseInvoice;

    public TempPaymentsDto(TempPaymentsEntity paymentsEntity){
        setPaymentId(paymentsEntity.getPaymentId());
        setPaymentType(paymentsEntity.getPaymentType());
        setPaidAmount(paymentsEntity.getPaidAmount());
        setPaidDate(paymentsEntity.getPaidDate());
        setTempSalesInvoice(new TempInvoiceDto(paymentsEntity.getTempSalesInvoice()));
    }
}
