package com.appli.clcapi.purchase.entity;


import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayCardEntity;
import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayCashEntity;
import com.appli.clcapi.paymentMethod.purchasePayMethods.entity.PurchasePayChequeEntity;
import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import com.appli.clcapi.purchase.dto.ConfirmPurchaseDto;
import com.appli.clcapi.vendor.entity.VendorEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "confirm_purchase_tbl")
@Builder
public class ConfirmPurchaseEntity {
    @Id
    private Long confirmPurchaseId;

    private Long purchaseInvoice;
    private Date purchaseDate;
    @ManyToOne
    @JoinColumn(name = "vendorId")
    private VendorEntity vendorEntity;

    private Double paidAmount;

    private Double totalAmount;


    //Payment Entity-->  Pay(m)----->confirmPurchase(1)
    @OneToMany(mappedBy = "confirmPurchaseEntity")
    private List<PurchasePaymentEntity> purchasePaymentEntity;

    @OneToMany(mappedBy = "confirmPurchaseEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchasePayCardEntity> purchasePayCardEntity;

    @OneToMany(mappedBy = "confirmPurchaseEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchasePayChequeEntity> purchasePayChequeEntity;

    @OneToMany(mappedBy = "confirmPurchaseEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PurchasePayCashEntity> purchasePayCashEntity;

    @OneToMany(mappedBy = "confirmPurchaseEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<ConfirmPurchaseProductCartEntity> confirmPurchaseProductCartEntity;

    public ConfirmPurchaseEntity(ConfirmPurchaseDto confirmPurchaseDto) {
        this.confirmPurchaseId = confirmPurchaseDto.getConfirmPurchaseId();
        this.purchaseInvoice = confirmPurchaseDto.getPurchaseInvoice();
        this.purchaseDate = confirmPurchaseDto.getPurchaseDate();
        this.vendorEntity = new VendorEntity(confirmPurchaseDto.getVendorEntity());
        this.paidAmount = confirmPurchaseDto.getPaidAmount();
        this.totalAmount = confirmPurchaseDto.getTotalAmount();

    }
}
