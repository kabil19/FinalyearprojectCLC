package com.appli.clcapi.payments.purchasePayment.entity;

import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import com.appli.clcapi.vendor.entity.VendorEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "confirm_purchase_payments_details_tbl")
@Builder
public class PurchasePaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;
    private String paymentType;
    private LocalDateTime paidDate;
    private Double paidAmount;

    @ManyToOne
    @JoinColumn(name="confirmPurchaseId")
    private ConfirmPurchaseEntity confirmPurchaseEntity;

    @ManyToOne
    @JoinColumn(name="vendorId")
    private VendorEntity vendorEntity;

}
