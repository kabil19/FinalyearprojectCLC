package com.appli.clcapi.purchaseProductCart.tempPurchase.dto;

import com.appli.clcapi.purchase.dto.TempPurchaseDto;
import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import com.appli.clcapi.stock.dto.StockDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TempPurchaseProductCartDto {
    @JsonProperty("productCartId")
    private Long productCartId;
    @JsonProperty("quantity")
    private Double quantity;
    @JsonProperty("discount")
    private Double discount;
    @JsonProperty("grossAmount")
    private Double grossAmount;
    @JsonProperty("netAmount")
    private Double netAmount;
    @JsonProperty("stockOBJ")
    private StockDto stockDto;
    @JsonProperty("tempPurchaseOBJ")
    private TempPurchaseDto tempPurchaseEntity;

    public TempPurchaseProductCartDto(TempPurchaseProductCartEntity tempPurchaseProductCartEntity) {
        this.productCartId = tempPurchaseProductCartEntity.getProductCartId();
        this.quantity = tempPurchaseProductCartEntity.getQuantity();
        this.discount = tempPurchaseProductCartEntity.getDiscount();
        this.grossAmount = tempPurchaseProductCartEntity.getGrossAmount();
        this.netAmount = tempPurchaseProductCartEntity.getNetAmount();
        this.stockDto = new StockDto(tempPurchaseProductCartEntity.getStockEntity());
        this.tempPurchaseEntity = new TempPurchaseDto(tempPurchaseProductCartEntity.getTempPurchaseEntity());
    }
}
