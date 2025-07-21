package com.example.jewelry_management.repository;

import com.example.jewelry_management.dto.res.AllCategoryNameResponse;
import com.example.jewelry_management.model.Category;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer>, JpaSpecificationExecutor<Category> {
    Category findByName(@NotBlank(message = "Trường tên thể loại không được bỏ trống") String name);

    @Query("SELECT c.id, c.name FROM Category c")
    List<AllCategoryNameResponse> findAllCategoryNames();

    Optional<Category> findByIdAndIsDeletedFalse(Integer id);

    List<Category> findByParentIdInAndIsDeletedFalse(List<Integer> ids);

    boolean existsByNameAndIsDeletedFalse(String name);

    List<Category> findAllByIdInAndIsDeletedFalse(List<Integer> ids);

    List<Category> findAllByIdInAndIsDeletedTrue(List<Integer> ids);

    List<Category> findByParentIsNullAndIsDeletedFalse();

    List<Category> findByParentIdAndIsDeletedFalse(Integer id);
}
