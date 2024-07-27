package com.appli.clcapi.paymentMethod.purchasePayMethods.dto;

import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayChequeEntity;
import com.appli.clcapi.purchase.dto.ConfirmPurchaseDto;
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
public class PurchasePayChequeDto {
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

    @JsonProperty("confirmPurchaseInvoiceOBJ")
    private ConfirmPurchaseDto confirmPurchaseDto;

    public PurchasePayChequeDto(PurchasePayChequeEntity purchasePayChequeEntity) {
        this.chequeRefNo = purchasePayChequeEntity.getChequeRefNo();
        this.paidAmount = purchasePayChequeEntity.getPaidAmount();
        this.paidDate = purchasePayChequeEntity.getPaidDate();
        this.chequeDueDate = purchasePayChequeEntity.getChequeDueDate();
        this.paymentId = purchasePayChequeEntity.getPaymentId();
        setConfirmPurchaseDto(new ConfirmPurchaseDto(purchasePayChequeEntity.getConfirmPurchaseEntity()));
    }
}
