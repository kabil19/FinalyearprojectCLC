package com.appli.clcapi.stock.repository;

import com.appli.clcapi.stock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StockRepo extends PagingAndSortingRepository<StockEntity, Long>, JpaRepository<StockEntity, Long> {

    List<StockEntity> findAllByDeletedEquals(boolean state);

    List<StockEntity> findAllBySellingPriceBetween(Double start, Double end);
    List<StockEntity> findAllByMaterialColourContainingIgnoreCaseOrItemNameContainingIgnoreCaseOrRemarksContainingIgnoreCase
            (String color, String name, String remarks);


    /*List<StockEntity> findAllByMaterialColourContainingIgnoreCaseOrMaterialTypeContainingIgnoreCaseOrRemarksContainingIgnoreCaseOrMaterialNameCategoryName
            (String color,String type,String remarks,String catName);*/


}
