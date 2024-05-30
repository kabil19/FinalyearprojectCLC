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
@Table(name = "temp_cheque_tbl")
public class TempChequeEntity {

    @Id
    private Long chequeRefNo;

    private Double paidAmount;

    private Date paidDate;

    private Date chequeDueDate;

    private Long paymentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tempInvoiceId")
    private TempInvoiceEntity tempInvoiceEntity;

}
