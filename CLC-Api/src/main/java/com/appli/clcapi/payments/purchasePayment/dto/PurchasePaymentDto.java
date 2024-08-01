package com.appli.clcapi.payments.purchasePayment.dto;


import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import com.appli.clcapi.purchase.dto.ConfirmPurchaseDto;
import com.appli.clcapi.vendor.dto.VendorDto;
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
public class PurchasePaymentDto {
    @JsonProperty("paymentId")
    private Long paymentId;
    @JsonProperty("paymentType")
    private String paymentType;
    @JsonProperty("paidDate")
    private LocalDateTime paidDate;
    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("ConfirmPurchaseOBJ")
    private ConfirmPurchaseDto confirmPurchaseDto;
    @JsonProperty("vendorOBJ")
    private VendorDto vendorDto;
    @JsonProperty("chequeRefNo")
    private Long chequeRefNo;
    @JsonProperty("chequeDueDate")
    private LocalDateTime chequeDueDate;
    @JsonProperty("cardRefNo")
    private Long cardRefNo;


    public PurchasePaymentDto(PurchasePaymentEntity purchasePaymentEntity) {
        this.setPaymentId(purchasePaymentEntity.getPaymentId());
        this.setPaymentType(purchasePaymentEntity.getPaymentType());
        this.setPaidDate(purchasePaymentEntity.getPaidDate());
        this.setPaidAmount(purchasePaymentEntity.getPaidAmount());
        this.setVendorDto(new VendorDto(purchasePaymentEntity.getVendorEntity()));
        this.setConfirmPurchaseDto(new ConfirmPurchaseDto(purchasePaymentEntity.getConfirmPurchaseEntity()));

    }
}
