package com.appli.clcapi.purchase.entity;

import com.appli.clcapi.stock.entity.StockEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "confirm_purchase_cart_tbl")
@Builder
public class ConfirmPurchaseProductCartEntity {

    @Id
    private Long productCartId;
    private Double quantity;
    private Double discount;
    private Double purchasePrice;
    private Double sellingPrice;
    private Double grossAmount;
    private Double netAmount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stockEntityId")
    private StockEntity stockEntity;
    @ManyToOne
    @JoinColumn(name = "confirmPurchaseId")
    private ConfirmPurchaseEntity confirmPurchaseEntity;
}
