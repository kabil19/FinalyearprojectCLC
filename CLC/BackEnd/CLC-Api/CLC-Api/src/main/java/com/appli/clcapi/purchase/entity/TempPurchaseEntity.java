package com.appli.clcapi.purchase.entity;

import com.appli.clcapi.purchase.dto.TempPurchaseDto;
import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import com.appli.clcapi.vendor.entity.VendorEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "temp_purchase_tbl")
@Builder
public class TempPurchaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long purchaseId;

    private Long purchaseInvoiceNO;

    private Date purchasedDate;

    private Double totalAmount;

//    private Double paidAmount;

    @ManyToOne
    @JoinColumn(name = "vendorId")
    private VendorEntity vendorEntity;

    @OneToMany(mappedBy = "tempPurchaseEntity",cascade = CascadeType.ALL)
    private List<TempPurchaseProductCartEntity> tempPurchaseProductCartEntity;


    public TempPurchaseEntity(TempPurchaseDto tempPurchaseDto){
        this.setPurchaseId(tempPurchaseDto.getPurchaseId());
        this.setPurchasedDate(tempPurchaseDto.getPurchasedDate());
        this.setVendorEntity(new VendorEntity(tempPurchaseDto.getVendorDto()));
//        this.setPaidAmount(tempPurchaseDto.getPaidAmount());
        this.setPurchaseInvoiceNO(tempPurchaseDto.getPurchaseInvoiceNO());
        this.setTotalAmount(tempPurchaseDto.getTotalAmount());
    }
}
