package com.appli.clcapi.category.service;

import com.appli.clcapi.category.dto.CategoryDto;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

public interface CategoryService {
    ResponseEntity<String> register(CategoryDto categoryDto);

    ResponseEntity<String> delete(Long categoryId);

    ResponseEntity<String> update(CategoryDto categoryDto);

    List<CategoryDto> getAll();

    ArrayList<CategoryDto> selectCategory(String existingChar);
}
