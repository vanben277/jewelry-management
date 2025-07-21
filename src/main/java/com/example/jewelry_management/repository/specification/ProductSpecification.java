package com.example.jewelry_management.repository.specification;

import com.example.jewelry_management.enums.ProductStatus;
import com.example.jewelry_management.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductSpecification {
    public static Specification<Product> nameContains(String name) {
        return ((root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        });
    }

    public static Specification<Product> fromPrice(BigDecimal price) {
        return ((root, query, criteriaBuilder) -> {
            if (price == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), price);
        });
    }

    public static Specification<Product> toPrice(BigDecimal price) {
        return ((root, query, criteriaBuilder) -> {
            if (price == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("price"), price);
        });
    }

    public static Specification<Product> fromQuantity(Integer quantity) {
        return ((root, query, criteriaBuilder) -> {
            if (quantity == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("quantity"), quantity);
        });
    }

    public static Specification<Product> toQuantity(Integer quantity) {
        return ((root, query, criteriaBuilder) -> {
            if (quantity == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("quantity"), quantity);
        });
    }

    public static Specification<Product> fromDateOfEntry(LocalDate dateOfEntry) {
        return ((root, query, criteriaBuilder) -> {
            if (dateOfEntry == null) {
                return null;
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("dateOfEntry"), dateOfEntry);
        });
    }

    public static Specification<Product> toDateOfEntry(LocalDate dateOfEntry) {
        return ((root, query, criteriaBuilder) -> {
            if (dateOfEntry == null) {
                return null;
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("dateOfEntry"), dateOfEntry);
        });
    }

    public static Specification<Product> hasStatus(ProductStatus status) {
        return ((root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        });
    }

    public static Specification<Product> categoryIdEquals(Integer categoryId) {
        return ((root, query, criteriaBuilder) -> {
            if (categoryId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("categoryId"), categoryId);
        });
    }

    public static Specification<Product> notDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDeleted"));
    }
}
