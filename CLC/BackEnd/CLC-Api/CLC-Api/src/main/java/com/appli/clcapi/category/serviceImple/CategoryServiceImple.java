package com.appli.clcapi.category.serviceImple;

import com.appli.clcapi.category.dto.CategoryDto;
import com.appli.clcapi.category.entity.CategoryEntity;
import com.appli.clcapi.category.repository.CategoryRepo;
import com.appli.clcapi.category.service.CatergoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImple implements CatergoryService {

    private final CategoryRepo categoryRepo;
    @Override
    public String register(CategoryDto categoryDto) {
        try {
            var aCategory = CategoryEntity
                    .builder()
                    .categoryId(categoryDto.getCategoryId())
                    .categoryName(categoryDto.getCategoryName())
                    .description(categoryDto.getDescription()).build();
            categoryRepo.save(aCategory);
            return "Category has been Inserted";
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Hasn't been Inserted");
        }
    }

    @Override
    public String delete(Long categoryId) {
       try {
           CategoryEntity aCat = categoryRepo.getReferenceById(categoryId);
           aCat.setDeleted(true);
           categoryRepo.save(aCat);
           return "Selected Category has been deleted";
       }catch (Exception e){
           e.printStackTrace();
           throw new RuntimeException("Hasn't been deleted");
       }

    }

    @Override
    public String update(CategoryDto categoryDto) {
        Optional<CategoryEntity> foundCat= categoryRepo.findById(categoryDto.getCategoryId());
        CategoryEntity updatedCat;
        if(foundCat.isPresent()){
            updatedCat = foundCat.get();
            updatedCat.setCategoryName(categoryDto.getCategoryName());
            updatedCat.setDescription(categoryDto.getDescription());

        }else{
            return "Couldn't find the category";
        }
        categoryRepo.save(updatedCat);
        return "Selected Category has been Updated";
    }

    @Override
    public List<CategoryDto> getAll() {
        List<CategoryEntity> catList = categoryRepo.findAllByDeletedEquals(false);
        List<CategoryDto> categoryForView = new ArrayList<>();
        for(CategoryEntity aCat : catList){
            CategoryDto categoryDto = new CategoryDto(aCat);
            categoryForView.add(categoryDto);
        }
        return categoryForView;
    }

    @Override
    public ArrayList<CategoryDto> selectCategory(String existingChar) {
        Iterable<CategoryEntity> catList= categoryRepo.findByCategoryNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                existingChar,
                existingChar
        );
        ArrayList<CategoryDto> categoryForView = new ArrayList<>();
        for (CategoryEntity aCat : catList){
            if(!aCat.isDeleted()){
                CategoryDto categoryDto = new CategoryDto(aCat);
                categoryForView.add(categoryDto);
            }

        }
        return categoryForView;
    }
}
