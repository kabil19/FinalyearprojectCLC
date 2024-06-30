package com.appli.clcapi.paymentMethod.purchasePayMethods.entity;

import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_pay_cash_tbl")
public class PurchasePayCashEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cashId;

    private Double paidAmount;

    private LocalDateTime paidDate;

    private Long paymentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmPurchaseId")
    private ConfirmPurchaseEntity confirmPurchaseEntity;
}
