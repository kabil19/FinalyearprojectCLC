package com.appli.clcapi.category.controller;

import com.appli.clcapi.category.dto.CategoryDto;
import com.appli.clcapi.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/category/")
@CrossOrigin("http://localhost:4200")
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody CategoryDto categoryDto){
        return categoryService.register(categoryDto);
    }

    @DeleteMapping("delete/{categoryId}")
    public ResponseEntity<String> delete(@PathVariable Long categoryId){
        return categoryService.delete(categoryId);
    }

    @PutMapping("update")
    public ResponseEntity<String> update(@RequestBody  CategoryDto categoryDto){
        return categoryService.update(categoryDto);
    }

    @GetMapping("getAll")
    public List<CategoryDto> getAll(){
        return categoryService.getAll();
    }

    @GetMapping("select/{existingChar}")
    public ArrayList<CategoryDto> selectCategory(@PathVariable String existingChar){
        return categoryService.selectCategory(existingChar);
    }
}
