package com.example.jewelry_management.dto.response;

import com.example.jewelry_management.model.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Integer id;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private LocalDate dateOfEntry;
    private String image;
    private String description;
    private ProductStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Integer categoryId;
    private Boolean isDeleted;
}
