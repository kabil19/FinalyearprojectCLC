package com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.dto;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmChequeEntity;
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
public class ConfirmChequeDto {
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

    @JsonProperty("confirmInvoiceOBJ")
    private ConfirmInvoiceDto confirmInvoiceDto;

    public ConfirmChequeDto(ConfirmChequeEntity chequeEntity){
        setChequeRefNo(chequeEntity.getChequeRefNo());
        setPaidAmount(chequeEntity.getPaidAmount());
        setPaidDate(chequeEntity.getPaidDate());
        setChequeDueDate(chequeEntity.getChequeDueDate());
        setPaidAmount(chequeEntity.getPaidAmount());
        setConfirmInvoiceDto(new ConfirmInvoiceDto(chequeEntity.getConfirmInvoiceEntity()));

    }
}
