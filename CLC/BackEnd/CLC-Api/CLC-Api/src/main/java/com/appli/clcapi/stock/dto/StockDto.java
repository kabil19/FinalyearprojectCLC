package com.appli.clcapi.stock.dto;

import com.appli.clcapi.category.dto.CategoryDto;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
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

    @JsonProperty("itemName")
    private String itemName;

    @JsonProperty("categoryOBJ")
    private CategoryDto categoryOBJ;

    @JsonProperty("materialColour")
    private String materialColour;

    @JsonProperty("quantity")
    private Long quantity;

    @JsonProperty("purchasePrice")
    private Float purchasePrice;

    @JsonProperty("sellingPrice")
    private Float sellingPrice;

    @JsonProperty("reorderQty")
    private Long reorderQty;

    @JsonProperty("arrivalDate")
    private Date arrivalDate;

    @JsonIgnore
    private boolean deleted = false;
    //image

    @JsonProperty("remarks")
    private String remarks;


    public StockDto(StockEntity stockEntity){
       setStockId(stockEntity.getStockId());
       setCategoryOBJ(new CategoryDto(stockEntity.getCategoryEntity()));
       setItemName(stockEntity.getItemName());
       setMaterialColour(stockEntity.getMaterialColour());
       setArrivalDate(stockEntity.getArrivalDate());
       setPurchasePrice(stockEntity.getPurchasePrice());
       setSellingPrice(stockEntity.getSellingPrice());
       setReorderQty(stockEntity.getReorderQty());
       setQuantity(stockEntity.getQuantity());
       setRemarks(stockEntity.getRemarks());
    }


}
