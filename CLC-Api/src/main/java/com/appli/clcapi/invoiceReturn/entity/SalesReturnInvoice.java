package com.appli.clcapi.invoiceReturn.entity;

import com.appli.clcapi.invoiceReturn.dto.SalesReturnInvoiceDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name="sales_return_invoice")
@AllArgsConstructor
@NoArgsConstructor
public class SalesReturnInvoice {
    @Id
    private Long salesReturnInvoiceId;
    private String invoiceReference;
    private LocalDateTime date;
    private Double netAmount;
    private Double paidAmount;
    private Double mainDiscount;
    private Double advancePayment;
    private Double returnAmount;
    @OneToMany(mappedBy = "salesReturnInvoice",cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SalesInvoiceCartReturnEntity> salesInvoiceCartReturnEntity;

    public SalesReturnInvoice(SalesReturnInvoiceDto salesReturnInvoiceDto) {
        this.setSalesReturnInvoiceId(salesReturnInvoiceDto.getSalesReturnInvoiceId());
        this.setInvoiceReference(salesReturnInvoiceDto.getInvoiceReference());
        this.setDate(salesReturnInvoiceDto.getDate());
        this.setNetAmount(salesReturnInvoiceDto.getNetAmount());
        this.setPaidAmount(salesReturnInvoiceDto.getPaidAmount());
        this.setMainDiscount(salesReturnInvoiceDto.getMainDiscount());
        this.setAdvancePayment(salesReturnInvoiceDto.getReturnAmount());
        this.setReturnAmount(salesReturnInvoiceDto.getReturnAmount());
    }
}
