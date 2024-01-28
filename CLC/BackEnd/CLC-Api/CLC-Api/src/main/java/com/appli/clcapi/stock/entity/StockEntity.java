package com.appli.clcapi.stock.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "stock_tbl")
@Builder
@Data
public class StockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long stockId;
    private String materialType;
    private String materialName;
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
