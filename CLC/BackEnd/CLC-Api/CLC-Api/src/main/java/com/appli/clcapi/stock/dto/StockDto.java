package com.appli.clcapi.stock.dto;

import com.appli.clcapi.stock.entity.StockEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockDto {

    @JsonProperty("stockId")
    private Long stockId;

    @JsonProperty("materialType")
    private String materialType;

    @JsonProperty("materialName")
    private String materialName;

    @JsonProperty("materialColour")
    private String materialColour;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("purchasePrice")
    private float purchasePrice;

    @JsonProperty("sellingPrice")
    private float sellingPrice;

    @JsonProperty("reorderQty")
    private int reorderQty;

    @JsonProperty("arrivalDate")
    private Date arrivalDate;

    @JsonIgnore
    private boolean deleted = false;
    //image

    @JsonProperty("remarks")
    private String remarks;


    public StockDto(StockEntity stockEntity){
       setStockId(stockEntity.getStockId());
       setMaterialName(stockEntity.getMaterialName());
       setMaterialType(stockEntity.getMaterialType());
       setMaterialColour(stockEntity.getMaterialColour());
       setArrivalDate(stockEntity.getArrivalDate());
       setPurchasePrice(stockEntity.getPurchasePrice());
       setSellingPrice(stockEntity.getSellingPrice());
       setReorderQty(stockEntity.getReorderQty());
       setQuantity(stockEntity.getQuantity());
       setRemarks(stockEntity.getRemarks());

    }

}
