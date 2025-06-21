package com.example.jewelry_management.repository;

import com.example.jewelry_management.model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    Category findByName(@NotBlank(message = "Trường tên thể loại không được bỏ trống") String name);

    @Query("SELECT c.name FROM Category c")
    List<String> findAllCategoryNames();


    Optional<Category> findByIdAndIsDeletedFalse(Integer id);

    List<Category> findByParentIdAndIsDeletedFalse(Integer id);
}
