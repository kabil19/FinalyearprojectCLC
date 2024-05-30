package com.appli.clcapi.paymentMethod.tempPayMethods.entity;

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
@Table(name = "temp_cash_tbl")
public class TempCashEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long cashId;

    private Double paidAmount;

    private Date paidDate;

    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tempInvoiceId")
    private TempInvoiceEntity tempInvoiceEntity;


}
