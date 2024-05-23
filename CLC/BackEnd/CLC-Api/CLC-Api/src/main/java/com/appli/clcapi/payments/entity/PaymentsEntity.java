package com.appli.clcapi.payments.entity;

import com.appli.clcapi.payments.dto.PaymentsDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments_details_tbl")
@Builder
public class PaymentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;
    private String paymentType;
    private Date paidDate;
    private Double paidAmount;
    @ManyToOne
    @JoinColumn(name="tempInvoiceId")
    private TempInvoiceEntity sellInvoice;
//    private Long purchaseInvoice;


    public PaymentsEntity(PaymentsDto paymentsDto) {
        this.paymentId = paymentsDto.getPaymentId();
        this.paymentType = paymentsDto.getPaymentType();
        this.paidDate = paymentsDto.getPaidDate();
        this.paidAmount = paymentsDto.getPaidAmount();
        this.sellInvoice = new TempInvoiceEntity(paymentsDto.getSellInvoice());
    }
}
