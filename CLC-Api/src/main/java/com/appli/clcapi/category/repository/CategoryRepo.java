package com.appli.clcapi.category.repository;

import com.appli.clcapi.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity,Long> {
    List<CategoryEntity> findAllByDeletedEquals(boolean state);
    Iterable<CategoryEntity> findByCategoryNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String desc);
}
