package com.appli.clcapi.category.entity;

import com.appli.clcapi.category.dto.CategoryDto;
import com.appli.clcapi.stock.entity.StockEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="category_tbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long categoryId;
    private String categoryName;
    private String description;
    private boolean deleted=false;
    @OneToMany(mappedBy = "categoryEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<StockEntity> stockEntity;


    public CategoryEntity(Long categoryId){
        this.categoryId = categoryId;
    }

    public CategoryEntity(CategoryDto categoryDto) {
        this.categoryId = categoryDto.getCategoryId();
        this.categoryName = categoryDto.getCategoryName();
        this.description = categoryDto.getDescription();
        this.deleted = categoryDto.isDeleted();
        this.stockEntity = null;
    }
}
