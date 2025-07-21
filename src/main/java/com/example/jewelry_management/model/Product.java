package com.example.jewelry_management.model;

import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.enums.ProductStatus;
import jakarta.persistence.*;
import lombok.Getter;
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
@Entity
@Table(name = "product")
@Slf4j
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "date_of_entry")
    private LocalDate dateOfEntry;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status = ProductStatus.IN_STOCK;

    @Column(name = "create_at", updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductSize> sizes = new ArrayList<>();

    @Column(name = "sku", nullable = false, unique = true)
    private String sku;

    @Enumerated(EnumType.STRING)
    @Column(name = "gold_type")
    private GoldType goldType;

    @PrePersist
    public void prePersist() {
        updateStatus();
    }

    @PreUpdate
    public void preUpdate() {
        updateStatus();
    }

    public void updateStatus() {
        int totalQuantity;
        if (sizes != null && !sizes.isEmpty()) {
            totalQuantity = sizes.stream()
                    .mapToInt(size -> size.getQuantity() != null ? size.getQuantity() : 0)
                    .sum();
        } else {
            totalQuantity = quantity != null ? quantity : 0;
        }
        setQuantity(totalQuantity);
        if (totalQuantity == 0 && status != ProductStatus.SOLD_OUT) {
            log.info("Product ID {} quantityInStock is 0, setting status to SOLD_OUT", id);
            status = ProductStatus.SOLD_OUT;
        } else if (totalQuantity > 0 && status == ProductStatus.SOLD_OUT) {
            log.info("Product ID {} quantityInStock is {}, setting status to IN_STOCK", id, totalQuantity);
            status = ProductStatus.IN_STOCK;
        }
    }
}
