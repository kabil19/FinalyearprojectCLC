package com.appli.clcapi.purchase.dto;

import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.vendor.dto.VendorDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempPurchaseDto {
    @JsonProperty("purchaseId")
    private Long purchaseId;

    @JsonProperty("purchaseInvoiceNO")
    private Long purchaseInvoiceNO;

    @JsonProperty("purchasedDate")
    private Date purchasedDate;

    @JsonProperty("netAmount")
    private Double netAmount;

   /*     @JsonProperty("paidAmount")
    private Double paidAmount;*/

    @JsonProperty("vendorOBJ")
    private VendorDto vendorDto;

    public TempPurchaseDto(TempPurchaseEntity tempPurchaseEntity) {
        this.purchaseId = tempPurchaseEntity.getPurchaseId();
        this.purchaseInvoiceNO = tempPurchaseEntity.getPurchaseInvoiceNO();
        this.purchasedDate = tempPurchaseEntity.getPurchasedDate();
        this.netAmount = tempPurchaseEntity.getNetAmount();
//        this.paidAmount = tempPurchaseEntity.getPaidAmount();
        this.vendorDto = new VendorDto(tempPurchaseEntity.getVendorEntity());
    }
}
