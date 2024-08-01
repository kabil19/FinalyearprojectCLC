package com.appli.clcapi.payments.purchasePayment.voucher.entity;

import com.appli.clcapi.payments.purchasePayment.voucher.dto.VoucherDto;
import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "voucher_details_tbl")
@Builder
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long voucherId;
    @ManyToOne
    @JoinColumn(name = "confirmPurchaseId")
    private ConfirmPurchaseEntity confirmPurchaseEntity;
    private Double paidAmount;
    private Date paidDate;
    private String paymentType;


    VoucherEntity(VoucherDto voucherDto){
        this.setVoucherId(voucherId);
        this.setConfirmPurchaseEntity(new ConfirmPurchaseEntity(voucherDto.getConfirmPurchaseDto()));
        this.setPaidAmount(voucherDto.getPaidAmount());
        this.setPaidDate(voucherDto.getPaidDate());
        this.setPaymentType(voucherDto.getPaymentType());
    }

}
