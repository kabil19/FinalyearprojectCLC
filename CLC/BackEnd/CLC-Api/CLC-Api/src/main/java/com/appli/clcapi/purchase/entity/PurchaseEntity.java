package com.appli.clcapi.purchase.entity;

import com.appli.clcapi.purchase.dto.PurchaseDto;
import com.appli.clcapi.vendor.entity.VendorEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "purchase_tbl")
@Builder
public class PurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseId;

    private Long purchaseInvoiceNO;

    private Date purchasedDate;

    private Double totalAmount;

    private Double paidAmount;

    @ManyToOne
    @JoinColumn(name = "vendorId")
    private VendorEntity vendorEntity;

    public PurchaseEntity(PurchaseDto purchaseDto){
        this.setPurchaseId(purchaseDto.getPurchaseId());
        this.setPurchasedDate(purchaseDto.getPurchasedDate());
        this.setVendorEntity(new VendorEntity(purchaseDto.getVendorDto()));
        this.setPaidAmount(purchaseDto.getPaidAmount());
        this.setPurchaseInvoiceNO(purchaseDto.getPurchaseInvoiceNO());
        this.setTotalAmount(purchaseDto.getTotalAmount());
    }
}
