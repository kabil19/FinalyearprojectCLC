package com.appli.clcapi.payments.purchasePayment.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private Date paidDate;
    private Double paidAmount;
    @ManyToOne
    @JoinColumn(name="confirmPurchaseId")
    private ConfirmPurchaseEntity confirmPurchaseEntity;

}
