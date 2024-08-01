package com.appli.clcapi.stock.entity;

import com.appli.clcapi.category.entity.CategoryEntity;
import com.appli.clcapi.productCart.entity.ProductCartEntity;
import com.appli.clcapi.purchaseProductCart.tempPurchase.entity.TempPurchaseProductCartEntity;
import com.appli.clcapi.stock.dto.StockDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stock_tbl")
@Builder
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stockId;
    private String itemName;
    @ManyToOne
    @JoinColumn(name="categoryId", nullable = false)
    private CategoryEntity categoryEntity;
    private String materialColour;
    private Double quantity;
    private Double purchasePrice;
    private Double sellingPrice;
    private Double reorderQty;
    private LocalDateTime arrivalDate;
    private Boolean deleted = false;
    //image
    private String remarks;

    @OneToMany(mappedBy = "stockEntity",cascade = CascadeType.ALL)
    private List<ProductCartEntity> productCartEntity;
    @OneToMany(mappedBy = "stockEntity",cascade = CascadeType.ALL)
    private List<TempPurchaseProductCartEntity> tempPurchaseProductCartEntity;






    public StockEntity(StockDto stockDto){
        this.setStockId(stockDto.getStockId());
        this.setItemName(stockDto.getItemName());
        this.setCategoryEntity(new CategoryEntity(stockDto.getCategoryOBJ()));
        this.setQuantity(stockDto.getQuantity());
        this.setReorderQty(stockDto.getReorderQty());
        this.setSellingPrice(stockDto.getSellingPrice());
        this.setPurchasePrice(stockDto.getPurchasePrice());
        this.setMaterialColour(stockDto.getMaterialColour());
        this.setArrivalDate(stockDto.getArrivalDate());

    }



}
