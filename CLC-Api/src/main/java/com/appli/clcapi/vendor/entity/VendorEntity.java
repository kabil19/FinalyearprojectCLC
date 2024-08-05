package com.appli.clcapi.vendor.entity;

import com.appli.clcapi.payments.purchasePayment.entity.PurchasePaymentEntity;
import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.vendor.dto.VendorDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Builder
@Table(name = "vendor_tbl")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VendorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long vendorId;
    private String vendorName;
    private String address;
    private String email;
    private  String contact;
    private boolean deleted = false;

    @OneToMany(mappedBy = "vendorEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<TempPurchaseEntity> tempPurchaseEntity;

    @OneToMany(mappedBy = "vendorEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY )
    private List<PurchasePaymentEntity> purchasePaymentEntity;
    public VendorEntity(VendorDto vendorDto) {
        this.setVendorId(vendorDto.getVendorId());
        this.setVendorName(vendorDto.getVendorName());
        this.setAddress(vendorDto.getAddress());
        this.setEmail(vendorDto.getEmail());
        this.setContact(vendorDto.getContact());
        this.setDeleted(vendorDto.isDeleted());
    }


}
