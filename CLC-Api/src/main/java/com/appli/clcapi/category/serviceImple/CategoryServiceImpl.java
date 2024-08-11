package com.appli.clcapi.category.serviceImple;
import com.appli.clcapi.category.dto.CategoryDto;
import com.appli.clcapi.category.entity.CategoryEntity;
import com.appli.clcapi.category.repository.CategoryRepo;
import com.appli.clcapi.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;
    @Override
    public ResponseEntity<String> register(CategoryDto categoryDto) {
        try {
            if(categoryDto.getCategoryName().isEmpty()){
                return new ResponseEntity<>("Name Can't be empty!", HttpStatus.BAD_REQUEST);
            }
            var aCategory = CategoryEntity
                    .builder()
                    .categoryId(categoryDto.getCategoryId())
                    .categoryName(categoryDto.getCategoryName())
                    .description(categoryDto.getDescription())
                    .build();
            categoryRepo.save(aCategory);
            return new ResponseEntity<>("Category is created!", HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>("Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @Override
    public ResponseEntity<String> delete(Long categoryId) {
       try {
           CategoryEntity aCat = categoryRepo.getReferenceById(categoryId);
           if(aCat.getStockEntity().isEmpty()){
               aCat.setDeleted(true);
               categoryRepo.save(aCat);
               return new ResponseEntity<>("Selected Category is deleted!",HttpStatus.OK);
           }else {
               return new ResponseEntity<>("Selected Category is used for the references of the stock!",HttpStatus.BAD_REQUEST);
           }

       }catch (Exception e){
           e.printStackTrace();
           return new ResponseEntity<>("Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);

       }
    }
    @Override
    public ResponseEntity<String> update(CategoryDto categoryDto) {
        try{
            if (categoryDto.getCategoryName().isEmpty()) {
                return new ResponseEntity<>("Name Can't be empty!", HttpStatus.BAD_REQUEST);
            }
            Optional<CategoryEntity> foundCat = categoryRepo.findById(categoryDto.getCategoryId());
            CategoryEntity updatedCat;
            if (foundCat.isPresent()) {
                updatedCat = foundCat.get();
                updatedCat.setCategoryName(categoryDto.getCategoryName());
                updatedCat.setDescription(categoryDto.getDescription());
                categoryRepo.save(updatedCat);
                return new ResponseEntity<>("Selected Category is updated!", HttpStatus.OK);
            }
        }catch (Exception e){
         e.printStackTrace();
            return new ResponseEntity<>("Server Error!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return null;
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
        Iterable<CategoryEntity> catList = categoryRepo.findByCategoryNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
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