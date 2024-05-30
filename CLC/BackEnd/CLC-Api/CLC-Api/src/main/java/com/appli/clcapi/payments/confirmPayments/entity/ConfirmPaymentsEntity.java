package com.appli.clcapi.payments.confirmPayments.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.payments.confirmPayments.dto.ConfirmPaymentsDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "confirm_payments_details_tbl")
@Builder
public class ConfirmPaymentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;
    private String paymentType;
    private Date paidDate;
    private Double paidAmount;


    @ManyToOne
    @JoinColumn(name="confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoice;
//    private Long purchaseInvoice;




    public ConfirmPaymentsEntity(ConfirmPaymentsDto confirmPaymentsDto) {
        this.paymentId = confirmPaymentsDto.getPaymentId();
        this.paymentType = confirmPaymentsDto.getPaymentType();
        this.paidDate = confirmPaymentsDto.getPaidDate();
        this.paidAmount = confirmPaymentsDto.getPaidAmount();

    }
}
