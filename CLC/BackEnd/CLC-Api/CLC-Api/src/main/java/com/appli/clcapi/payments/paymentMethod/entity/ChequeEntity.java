package com.appli.clcapi.payments.paymentMethod.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cheque_tbl")
public class ChequeEntity {

    @Id
    private Long chequeRefNo;

    private Double paidAmount;

    private Date paidDate;

    private Date chequeDueDate;

    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tempInvoiceId")
    private TempInvoiceEntity tempInvoiceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoiceEntity;
}
