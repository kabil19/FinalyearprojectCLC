package com.appli.clcapi.paymentMethod.confirmPayMethods.entity;

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
@Table(name = "confirm_card_tbl")
public class ConfirmCardEntity {

    @Id
    private Long cardRefNo;

    private Double paidAmount;
    private Date paidDate;

    private Long paymentId;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoiceEntity;

//    have to add purchase the similar way i have added the tempInvoice




}
