package com.appli.clcapi.category.controller;

import com.appli.clcapi.category.dto.CategoryDto;
import com.appli.clcapi.category.service.CatergoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/category/")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CatergoryService catergoryService;
    @PostMapping("register")
    public String register(@RequestBody CategoryDto categoryDto){
        return catergoryService.register(categoryDto);
    }

    @DeleteMapping("delete/{categoryId}")
    public String delete(@PathVariable Long categoryId){
        return catergoryService.delete(categoryId);
    }

    @PutMapping("update")
    public String update(@RequestBody  CategoryDto categoryDto){
        return catergoryService.update(categoryDto);
    }

    @GetMapping("getAll")
    public List<CategoryDto> getAll(){
        return catergoryService.getAll();
    }

    @GetMapping("select/{existingChar}")
    public ArrayList<CategoryDto> selectCategory(@PathVariable String existingChar){
        return catergoryService.selectCategory(existingChar);
    }
}
