package com.appli.clcapi.payments.paymentMethod.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.payments.entity.PaymentsEntity;
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
@Table(name = "card_tbl")
public class CardEntity {

    @Id
    private Long cardRefNo;

    private Double paidAmount;
    private Date paidDate;

    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tempInvoiceId")
    private TempInvoiceEntity tempInvoiceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoiceEntity;

//    have to add purchase the similar way i have added the tempInvoice




}
