package com.example.jewelry_management.model;

import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "product")
@Slf4j
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @Column(name = "cost_price", precision = 15, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "date_of_entry", nullable = false)
    private LocalDate dateOfEntry;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status = ProductStatus.IN_STOCK;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt;

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(name = "gold_type")
    private GoldType goldType;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSize> sizes = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        updateStatus();
    }

    @PreUpdate
    public void preUpdate() {
        updateStatus();
    }

    public void updateStatus() {
        int totalQuantity = 0;
        if (sizes != null && !sizes.isEmpty()) {
            totalQuantity = sizes.stream()
                    .mapToInt(size -> size.getQuantity() != null ? size.getQuantity() : 0)
                    .sum();
        } else {
            totalQuantity = (quantity != null) ? quantity : 0;
        }

        this.quantity = totalQuantity;

        if (totalQuantity <= 0) {
            this.status = ProductStatus.SOLD_OUT;
        } else {
            if (this.status == ProductStatus.SOLD_OUT) {
                this.status = ProductStatus.IN_STOCK;
            }
        }
    }
}