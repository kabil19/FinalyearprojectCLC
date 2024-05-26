package com.appli.clcapi.confirmInvoice.entity;

import com.appli.clcapi.confirmInvoice.dto.ConfirmInvoiceDto;
import com.appli.clcapi.confirmInvoice.confirmCartItems.entity.ConfirmProductCartEntity;
import com.appli.clcapi.customer.entity.CustomerEntity;
import com.appli.clcapi.payments.entity.PaymentsEntity;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
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

    @OneToMany(mappedBy = "confirmInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductCartEntity> productCartEntity;

    @ManyToOne
    @JoinColumn(name="custId")
    private CustomerEntity customer;

    @OneToMany(mappedBy = "confirmInvoice", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PaymentsEntity> paymentsEntity;

    @OneToMany(mappedBy = "confirmInvoiceEntity",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ConfirmProductCartEntity> confirmProductCartEntity;


    public ConfirmInvoiceEntity(ConfirmInvoiceDto confirmInvoiceDto) {
        this.confirmInvoiceId = confirmInvoiceDto.getConfirmInvoiceId();
        this.invoiceNumber = confirmInvoiceDto.getInvoiceNumber();
        this.date = confirmInvoiceDto.getDate();
        this.netAmount = confirmInvoiceDto.getNetAmount();
        this.paidAmount = confirmInvoiceDto.getPaidAmount();
        this.customer = new CustomerEntity(confirmInvoiceDto.getCustomerOBJ());
    }
}
