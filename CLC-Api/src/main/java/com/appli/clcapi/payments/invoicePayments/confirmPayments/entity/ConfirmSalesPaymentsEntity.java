package com.appli.clcapi.payments.invoicePayments.confirmPayments.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmSalesInvoiceEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.dto.ConfirmSalesPaymentsDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "confirm_payments_details_tbl")
@Builder
public class ConfirmSalesPaymentsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long paymentId;
    private String paymentType;
    private LocalDateTime paidDate;
    private Double paidAmount;


    @ManyToOne
    @JoinColumn(name="confirmInvoiceId")
    private ConfirmSalesInvoiceEntity confirmSalesInvoiceEntity;
//    private Long purchaseInvoice;




    public ConfirmSalesPaymentsEntity(ConfirmSalesPaymentsDto confirmSalesPaymentsDto) {
        this.paymentId = confirmSalesPaymentsDto.getPaymentId();
        this.paymentType = confirmSalesPaymentsDto.getPaymentType();
        this.paidDate = confirmSalesPaymentsDto.getPaidDate();
        this.paidAmount = confirmSalesPaymentsDto.getPaidAmount();

    }
}
