package com.appli.clcapi.invoiceReturn.entity;


import com.appli.clcapi.invoiceReturn.dto.SalesInvoiceCartReturnDto;

import com.appli.clcapi.stock.entity.StockEntity;
import jakarta.persistence.*;
import lombok.*;



@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sales_invoice_cart_return_tbl")
public class SalesInvoiceCartReturnEntity {

    @Id
    private Long salesRetProductCartId;
    private Double quantity;
    private Double discount;
    private Double total;
    private Double netAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockEntityId")
    private StockEntity stockEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="salesReturnInvoiceId")
    private SalesReturnInvoice salesReturnInvoice;


    public SalesInvoiceCartReturnEntity(SalesInvoiceCartReturnDto salesInvoiceCartReturnDto) {
        this.salesRetProductCartId = salesInvoiceCartReturnDto.getSalesRetProductCartId();
        this.quantity = salesInvoiceCartReturnDto.getQuantity();
        this.discount = salesInvoiceCartReturnDto.getDiscount();
        this.total = salesInvoiceCartReturnDto.getTotal();
        this.netAmount = salesInvoiceCartReturnDto.getNetAmount();
        this.stockEntity = new StockEntity(salesInvoiceCartReturnDto.getStockDto());
        this.salesReturnInvoice = new SalesReturnInvoice(salesInvoiceCartReturnDto.getSalesReturnInvoiceDto());
    }
}
