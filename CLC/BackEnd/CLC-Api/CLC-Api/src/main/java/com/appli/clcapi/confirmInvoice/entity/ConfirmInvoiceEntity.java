package com.appli.clcapi.confirmInvoice.entity;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCardEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmCashEntity;
import com.appli.clcapi.paymentMethod.invoicePayMethods.confirmPayMethods.entity.ConfirmChequeEntity;
import com.appli.clcapi.payments.invoicePayments.confirmPayments.entity.ConfirmPaymentsEntity;
import com.appli.clcapi.payments.invoicePayments.receipt.entity.ConfirmSalesInvoiceReceiptEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name="confirm_invoice_tbl")
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmInvoiceEntity {

    @Id
    private Long confirmInvoiceId;
    private Long invoiceNumber;
    private Date date;
    private Double netAmount;
    private Double paidAmount;

  /*  @OneToMany(mappedBy = "confirmInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCartEntity> productCartEntity;*/
    private Boolean isComplete =false;

    @ManyToOne
    @JoinColumn(name="custId")
    private CustomerEntity customer;

    @OneToMany(mappedBy ="confirmInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmSalesInvoiceReceiptEntity> confirmSalesInvoiceReceiptEntity;

    @OneToMany(mappedBy = "confirmInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmPaymentsEntity> confirmPaymentsEntity;

    @OneToMany(mappedBy = "confirmInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmProductCartEntity> confirmProductCartEntity;

    @OneToMany(mappedBy = "confirmInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmCardEntity> confirmCardEntity;//checked

    @OneToMany(mappedBy = "confirmInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmCashEntity> confirmCashEntity;//checked

    @OneToMany(mappedBy = "confirmInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmChequeEntity> confirmChequeEntity;//checked


    public ConfirmInvoiceEntity(ConfirmInvoiceDto confirmInvoiceDto) {
        this.confirmInvoiceId = confirmInvoiceDto.getConfirmInvoiceId();
        this.invoiceNumber = confirmInvoiceDto.getInvoiceNumber();
        this.date = confirmInvoiceDto.getDate();
        this.netAmount = confirmInvoiceDto.getNetAmount();
        this.paidAmount = confirmInvoiceDto.getPaidAmount();
        this.customer = new CustomerEntity(confirmInvoiceDto.getCustomerOBJ());
        this.isComplete = confirmInvoiceDto.getIsComplete();
    }



}
