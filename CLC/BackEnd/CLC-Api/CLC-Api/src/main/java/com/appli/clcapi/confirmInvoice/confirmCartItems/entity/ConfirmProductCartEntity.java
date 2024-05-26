package com.appli.clcapi.confirmInvoice.confirmCartItems.entity;

import com.appli.clcapi.confirmInvoice.entity.ConfirmInvoiceEntity;
import com.appli.clcapi.confirmInvoice.confirmCartItems.dto.ConfirmProductCartDto;
import com.appli.clcapi.stock.entity.StockEntity;
import com.appli.clcapi.tempInvoice.entity.TempInvoiceEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name="confirm_pro_cart_tbl")
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmProductCartEntity {

    @Id
    private Long confirmProductCartId;
    private Double quantity;
    private Double discount;
    private Double total;
    private Double netAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tempInvoiceId")
    private TempInvoiceEntity tempInvoiceEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockEntityId")
    private StockEntity stockEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="confirmInvoiceId")
    private ConfirmInvoiceEntity confirmInvoiceEntity;

    public ConfirmProductCartEntity(ConfirmProductCartDto confirmProductCartDto) {
        this.confirmProductCartId = confirmProductCartDto.getConfirmProductCartId();
        this.quantity = confirmProductCartDto.getQuantity();
        this.discount = confirmProductCartDto.getDiscount();
        this.total = confirmProductCartDto.getTotal();
        this.netAmount = confirmProductCartDto.getNetAmount();
        this.tempInvoiceEntity = new TempInvoiceEntity(confirmProductCartDto.getTempInvoiceDto());
        this.stockEntity = new StockEntity(confirmProductCartDto.getStockDto());
        this.confirmInvoiceEntity = new ConfirmInvoiceEntity(confirmProductCartDto.getConfirmInvoiceDto());
    }

}
