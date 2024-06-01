package com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "confirm_cash_tbl")
public class ConfirmCashEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cashId;

    private Double paidAmount;

    private Date paidDate;

    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoiceEntity;


}
