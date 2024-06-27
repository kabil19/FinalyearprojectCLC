package com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "confirm_cheque_tbl")
public class ConfirmChequeEntity {


   /* @GeneratedValue(strategy = GenerationType.AUTO)
    private Long checkId;*/
    @Id
    private Long chequeRefNo;

    private Double paidAmount;

    private LocalDateTime paidDate;

    private LocalDateTime chequeDueDate;

    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoiceEntity;
}
