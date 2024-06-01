package com.appli.clcapi.paymentMethod.purchasePayMethods.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "purchase_pay_card_tbl")
public class PurchasePayCardEntity {
    @Id
    private Long cardRefNo;

    private Double paidAmount;
    private Date paidDate;

    private Long paymentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmPurchaseId")
    private ConfirmPurchaseEntity confirmPurchaseEntity;
}
