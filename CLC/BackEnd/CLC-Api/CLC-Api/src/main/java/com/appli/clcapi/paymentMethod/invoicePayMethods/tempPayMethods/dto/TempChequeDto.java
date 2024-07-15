package com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.dto;

import com.appli.clcapi.paymentMethod.invoicePayMethods.tempPayMethods.entity.TempChequeEntity;
import com.appli.clcapi.tempInvoice.dto.TempInvoiceDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TempChequeDto {
    @JsonProperty("chequeRefNo")
    private Long chequeRefNo;

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("paidDate")
    private LocalDateTime paidDate;

    @JsonProperty("chequeDueDate")
    private LocalDateTime chequeDueDate;

    @JsonProperty("paymentId")
    private Long paymentId;

    @JsonProperty("tempInvoiceOBJ")
    private TempInvoiceDto tempInvoiceDto;


    public TempChequeDto(TempChequeEntity tempChequeEntity) {
        this.chequeRefNo = tempChequeEntity.getChequeRefNo();
        this.paidAmount = tempChequeEntity.getPaidAmount();
        this.paidDate = tempChequeEntity.getPaidDate();
        this.chequeDueDate = tempChequeEntity.getChequeDueDate();
        this.paymentId = tempChequeEntity.getPaymentId();
        this.tempInvoiceDto = new TempInvoiceDto(tempChequeEntity.getTempInvoiceEntity());
    }
}
