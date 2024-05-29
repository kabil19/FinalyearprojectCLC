package com.appli.clcapi.purchase.dto;

import com.appli.clcapi.purchase.entity.PurchaseEntity;
import com.appli.clcapi.vendor.dto.VendorDto;
import com.appli.clcapi.vendor.entity.VendorEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDto {
    @JsonProperty("purchaseId")
    private Long purchaseId;

    @JsonProperty("purchaseInvoiceNO")
    private Long purchaseInvoiceNO;

    @JsonProperty("purchasedDate")
    private Date purchasedDate;

    @JsonProperty("totalAmount")
    private Double totalAmount;

    @JsonProperty("paidAmount")
    private Double paidAmount;

    @JsonProperty("vendorOBJ")
    private VendorDto vendorDto;

    public PurchaseDto(PurchaseEntity purchaseEntity) {
        this.purchaseId = purchaseEntity.getPurchaseId();
        this.purchaseInvoiceNO = purchaseEntity.getPurchaseInvoiceNO();
        this.purchasedDate = purchaseEntity.getPurchasedDate();
        this.totalAmount = purchaseEntity.getTotalAmount();
        this.paidAmount = purchaseEntity.getPaidAmount();
        this.vendorDto = new VendorDto(purchaseEntity.getVendorEntity());
    }
}
