package com.appli.clcapi.purchase.dto;

import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import com.appli.clcapi.vendor.dto.VendorDto;
import com.appli.clcapi.vendor.entity.VendorEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmPurchaseDto {
    @JsonProperty("confirmPurchaseId")
    private Long confirmPurchaseId;
    @JsonProperty("purchaseInvoice")
    private Long purchaseInvoice;
    @JsonProperty("purchaseDate")
    private Date purchaseDate;
    @JsonProperty("vendorOBJ")
    private VendorDto vendorEntity;
    @JsonProperty("paidAmount")
    private Double paidAmount;
    @JsonProperty("totalAmount")
    private Double totalAmount;

    public ConfirmPurchaseDto(ConfirmPurchaseEntity confirmPurchaseEntity) {
        this.confirmPurchaseId = confirmPurchaseEntity.getConfirmPurchaseId();
        this.purchaseInvoice = confirmPurchaseEntity.getPurchaseInvoice();
        this.purchaseDate = confirmPurchaseEntity.getPurchaseDate();
        this.vendorEntity = new VendorDto(confirmPurchaseEntity.getVendorEntity());
        this.paidAmount = confirmPurchaseEntity.getPaidAmount();
        this.totalAmount = confirmPurchaseEntity.getTotalAmount();
    }
}
