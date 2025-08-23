package com.example.jewelry_management.repository.specification;

import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.enums.ProductStatus;
import com.example.jewelry_management.model.Product;
import jakarta.persistence.criteria.Expression;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {
    public static Specification<Product> nameAndSkuCombinedSearch(String name) {
        return ((root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return null;
            }
            String likePattern = "%" + name.toLowerCase().trim() + "%";

            Expression<String> displayNameException = criteriaBuilder.concat(
                    criteriaBuilder.concat(root.get("name"), " "),
                    root.get("sku")
            );

            return criteriaBuilder.like(criteriaBuilder
                            .lower(
                                    displayNameException
                            ),
                    likePattern
            );
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

    public static Specification<Product> hasStatus(ProductStatus status) {
        return ((root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        });
    }

    public static Specification<Product> notDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDeleted"));
    }

    public static Specification<Product> goldTypeEquals(GoldType goldType) {
        return ((root, query, criteriaBuilder) -> {
            if (goldType == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("goldType"), goldType);
        });
    }

    public static Specification<Product> categoryEquals(Integer categoryId) {
        return (root, query, criteriaBuilder) ->
                categoryId == null ? null : criteriaBuilder.equal(root.get("category").get("id"), categoryId);
    }

    public static Specification<Product> isDeletedEquals(Boolean isDeleted) {
        return (root, query, cb) -> {
            if (isDeleted == null) {
                return null;
            }
            return cb.equal(root.get("isDeleted"), isDeleted);
        };
    }
}
