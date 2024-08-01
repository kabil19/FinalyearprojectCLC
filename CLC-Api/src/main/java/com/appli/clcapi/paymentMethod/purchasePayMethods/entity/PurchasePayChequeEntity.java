package com.appli.clcapi.paymentMethod.purchasePayMethods.entity;

import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_pay_cheque_tbl")
public class PurchasePayChequeEntity {
    @Id
    private Long chequeRefNo;

    private Double paidAmount;

    private LocalDateTime paidDate;

    private LocalDateTime chequeDueDate;

    private Long paymentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmPurchaseId")
    private ConfirmPurchaseEntity confirmPurchaseEntity;
}
