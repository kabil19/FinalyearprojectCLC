package com.appli.clcapi.category.dto;

import com.appli.clcapi.category.entity.CategoryEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    @JsonProperty("categoryId")
    private Long categoryId;
    @JsonProperty("categoryName")
    private String categoryName;
    @JsonProperty("description")
    private String description;

    @JsonProperty("deleted")
    @JsonIgnore
    private boolean deleted;

    public CategoryDto(CategoryEntity aCat) {
        setCategoryId(aCat.getCategoryId());
        setCategoryName(aCat.getCategoryName());
        setDescription(aCat.getDescription());
    }
}
