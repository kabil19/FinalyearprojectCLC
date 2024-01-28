package com.appli.clcapi.stock.repository;

import com.appli.clcapi.stock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockRepo extends JpaRepository<StockEntity, Long> {

    List<StockEntity> findAllByDeletedEquals(boolean state);
    List<StockEntity> findAllByMaterialColourContainingIgnoreCaseOrMaterialNameContainingIgnoreCaseOrMaterialTypeContainingIgnoreCaseOrRemarksContainingIgnoreCase
            (String color,String name,String type,String remarks);

}
