package com.appli.clcapi.stock.entity;

import com.appli.clcapi.category.entity.CategoryEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

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
    private int quantity;
    private float purchasePrice;
    private float sellingPrice;
    private int reorderQty;
    private Date arrivalDate;
    private boolean deleted = false;
    //image
    private String remarks;

}
