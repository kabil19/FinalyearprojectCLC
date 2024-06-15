package com.appli.clcapi.payments.purchasePayment.voucher.dto;


import com.appli.clcapi.payments.purchasePayment.voucher.entity.VoucherEntity;
import com.appli.clcapi.purchase.dto.ConfirmPurchaseDto;
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
public class VoucherDto {

    @JsonProperty("receiptId")
    private Long voucherId;
    @JsonProperty("confirmPurchaseOBJ")
    private ConfirmPurchaseDto confirmPurchaseDto;
    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("paidDate")
    private Date paidDate;
    @JsonProperty("paymentType")
    private String paymentType;

    public VoucherDto(VoucherEntity voucherEntity){
        this.setVoucherId(voucherEntity.getVoucherId());
        this.setPaidAmount(voucherEntity.getPaidAmount());
        this.setPaymentType(voucherEntity.getPaymentType());
        this.setConfirmPurchaseDto(new ConfirmPurchaseDto(voucherEntity.getConfirmPurchaseEntity()));
        this.setPaidDate(voucherEntity.getPaidDate());
    }

}
