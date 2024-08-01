package com.appli.clcapi.purchase.dto;

import com.appli.clcapi.purchase.entity.ConfirmPurchaseEntity;
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
public class ConfirmPurchaseProductCartDto {
    @JsonProperty("productCartId")
    private Long productCartId;
    @JsonProperty("quantity")
    private Double quantity;
    @JsonProperty("discount")
    private Double discount;
    @JsonProperty("purchasePrice")
    private Double purchasePrice;
    @JsonProperty("sellingPrice")
    private Double sellingPrice;
    @JsonProperty("grossAmount")
    private Double grossAmount;
    @JsonProperty("netAmount")
    private Double netAmount;
    @JsonProperty("stockOBJ")
    private StockDto stockDto;
    @JsonProperty("confirmPurchaseOBJ")
    private ConfirmPurchaseEntity confirmPurchaseEntity;
}
