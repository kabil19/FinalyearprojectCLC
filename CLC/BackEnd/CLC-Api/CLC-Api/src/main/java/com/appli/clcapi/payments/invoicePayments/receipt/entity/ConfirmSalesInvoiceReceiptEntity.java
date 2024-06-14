package com.appli.clcapi.payments.invoicePayments.receipt.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.payments.invoicePayments.receipt.dto.ConfirmSalesInvoiceReceiptDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "receipt_details_tbl")
@Builder
public class ConfirmSalesInvoiceReceiptEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long receiptId;
    @ManyToOne
    @JoinColumn(name = "confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoiceEntity;
    private Double paidAmount;
    private Date paidDate;
    private String paymentType;

    public ConfirmSalesInvoiceReceiptEntity(ConfirmSalesInvoiceReceiptDto confirmSalesInvoiceReceiptDto){
        this.setReceiptId(confirmSalesInvoiceReceiptDto.getReceiptId());
        this.setPaidAmount(confirmSalesInvoiceReceiptDto.getPaidAmount());
        this.setConfirmInvoiceEntity(new ConfirmInvoiceEntity(confirmSalesInvoiceReceiptDto.getConfirmInvoiceDto()));
        this.setPaymentType(confirmSalesInvoiceReceiptDto.getPaymentType());
        this.setPaidDate(new Date());
    }
}
