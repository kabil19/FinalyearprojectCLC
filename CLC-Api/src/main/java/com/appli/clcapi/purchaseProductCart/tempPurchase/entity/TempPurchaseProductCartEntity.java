package com.appli.clcapi.purchaseProductCart.tempPurchase.entity;

import com.appli.clcapi.purchase.entity.TempPurchaseEntity;
import com.appli.clcapi.purchaseProductCart.tempPurchase.dto.TempPurchaseProductCartDto;
import com.appli.clcapi.stock.entity.StockEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tempPurchase_productCart_tbl")
@Builder
public class TempPurchaseProductCartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productCartId;
    private Double quantity;
    private Double discount;
    private Double grossAmount;
    private Double netAmount;
    private Double purchasePrice;
    private Double sellingPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockEntityId")
    private StockEntity stockEntity;

    @ManyToOne
    @JoinColumn(name = "purchaseId")
    private TempPurchaseEntity tempPurchaseEntity;

    public TempPurchaseProductCartEntity(TempPurchaseProductCartDto tempPurchaseProductCartDto) {
        this.productCartId = tempPurchaseProductCartDto.getProductCartId();
        this.quantity = tempPurchaseProductCartDto.getQuantity();
        this.discount = tempPurchaseProductCartDto.getDiscount();
        this.grossAmount = tempPurchaseProductCartDto.getGrossAmount();
        this.netAmount = tempPurchaseProductCartDto.getNetAmount();
        this.stockEntity = new StockEntity(tempPurchaseProductCartDto.getStockDto());
        this.tempPurchaseEntity = new TempPurchaseEntity(tempPurchaseProductCartDto.getTempPurchaseEntity());
        this.sellingPrice = tempPurchaseProductCartDto.getPurchasePrice();
        this.purchasePrice = tempPurchaseProductCartDto.getPurchasePrice();
    }
}
