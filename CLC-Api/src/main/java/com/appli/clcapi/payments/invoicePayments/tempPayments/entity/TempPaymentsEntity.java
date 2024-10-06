package com.appli.clcapi.payments.invoicePayments.tempPayments.entity;

import com.appli.clcapi.payments.invoicePayments.tempPayments.dto.TempPaymentsDto;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "temp_payments_details_tbl")
@Builder
public class TempPaymentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;
    private String paymentType;
    private LocalDateTime paidDate;
    private Double paidAmount;
    @ManyToOne
    @JoinColumn(name="tempInvoiceId")
    private TempInvoiceEntity tempSalesInvoice;


//    private Long purchaseInvoice;

    public TempPaymentsEntity(TempPaymentsDto paymentsDto) {
        this.paymentId = paymentsDto.getPaymentId();
        this.paymentType = paymentsDto.getPaymentType();
        this.paidDate = paymentsDto.getPaidDate();
        this.paidAmount = paymentsDto.getPaidAmount();
        this.tempSalesInvoice = new TempInvoiceEntity(paymentsDto.getTempSalesInvoice());
    }
}
