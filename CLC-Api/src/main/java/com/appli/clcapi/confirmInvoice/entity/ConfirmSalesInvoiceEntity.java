package com.appli.clcapi.confirmInvoice.entity;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmSalesInvoiceChequeEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmSalesPaymentsEntity;
import com.appli.clcapi.payments.invoicePayments.receipt.entity.ConfirmSalesInvoiceReceiptEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name="confirm_invoice_tbl")
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmSalesInvoiceEntity {

    @Id
    private Long confirmInvoiceId;
    private String invoiceReference;
    private LocalDateTime date;
    private Double netAmount;
    private Double paidAmount;
    private Double mainDiscount;
    private Double advancePayment;
    private Double returnAmount;
    private Boolean isComplete =false;

    @ManyToOne
    @JoinColumn(name="custId")
    private CustomerEntity customer;

    @OneToMany(mappedBy ="confirmSalesInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmSalesInvoiceReceiptEntity> confirmSalesInvoiceReceiptEntity;

    @OneToMany(mappedBy = "confirmSalesInvoiceEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmSalesPaymentsEntity> confirmSalesPaymentsEntity;

    @OneToMany(mappedBy = "confirmSalesInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmProductCartEntity> confirmProductCartEntity;

    @OneToMany(mappedBy = "confirmSalesInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmCardEntity> confirmCardEntity;//checked

    @OneToMany(mappedBy = "confirmSalesInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmCashEntity> confirmCashEntity;//checked

    @OneToMany(mappedBy = "confirmSalesInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmSalesInvoiceChequeEntity> confirmSalesInvoiceChequeEntity;//checked


    public ConfirmSalesInvoiceEntity(ConfirmInvoiceDto confirmInvoiceDto) {
        this.confirmInvoiceId = confirmInvoiceDto.getConfirmInvoiceId();
        this.invoiceReference = confirmInvoiceDto.getInvoiceReference();
        this.date = confirmInvoiceDto.getDate();
        this.netAmount = confirmInvoiceDto.getNetAmount();
        this.paidAmount = confirmInvoiceDto.getPaidAmount();
        this.customer = new CustomerEntity(confirmInvoiceDto.getCustomerOBJ());
        this.isComplete = confirmInvoiceDto.getIsComplete();
        this.advancePayment = confirmInvoiceDto.getAdvanceAmount();
        this.mainDiscount = confirmInvoiceDto.getMainDiscount();
        this.returnAmount = confirmInvoiceDto.getReturnAmount();
    }



}
