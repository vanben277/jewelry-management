package com.example.jewelry_management.dto.res;

import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.enums.ProductStatus;
import com.example.jewelry_management.form.ProductImageForm;
import com.example.jewelry_management.form.ProductSizeForm;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private String description;
    private ProductStatus status;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Integer categoryId;
    private Boolean isDeleted;
    private String primaryImageUrl;
    private List<ProductImageForm> images = new ArrayList<>();
    private List<ProductSizeForm> sizes = new ArrayList<>();
    private String sku;
    private GoldType goldType;
    private String categoryName;

    @JsonProperty("displayName")
    public String getDisplayName() {
        return name + " " + sku;
    }
}
