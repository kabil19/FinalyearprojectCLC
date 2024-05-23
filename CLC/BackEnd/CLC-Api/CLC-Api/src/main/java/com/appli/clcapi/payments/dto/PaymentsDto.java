package com.appli.clcapi.payments.dto;

import com.appli.clcapi.payments.entity.PaymentsEntity;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
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
    @JsonProperty("sellInvoice")
    private TempInvoiceDto sellInvoice;
//    private Long purchaseInvoice;

    public PaymentsDto(PaymentsEntity paymentsEntity){
        setPaymentId(paymentsEntity.getPaymentId());
        setPaymentType(paymentsEntity.getPaymentType());
        setPaidAmount(paymentsEntity.getPaidAmount());
        setPaidDate(paymentsEntity.getPaidDate());
        setSellInvoice(new TempInvoiceDto(paymentsEntity.getSellInvoice()));
    }
}
