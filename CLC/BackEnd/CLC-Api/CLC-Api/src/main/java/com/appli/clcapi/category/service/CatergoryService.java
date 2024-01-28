package com.appli.clcapi.category.service;

import com.appli.clcapi.category.dto.CategoryDto;

import java.util.ArrayList;
import java.util.List;

public interface CatergoryService {
    String register(CategoryDto categoryDto);

    String delete(Long categoryId);

    String update(CategoryDto categoryDto);

    List<CategoryDto> getAll();

    ArrayList<CategoryDto> selectCategory(String existingChar);
}
