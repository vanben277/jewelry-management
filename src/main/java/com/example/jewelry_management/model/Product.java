package com.example.jewelry_management.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @Column(name = "image")
    private String image;

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

    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;


    @PrePersist
    public void prePersist() {
        updateStatus();
    }

    @PreUpdate
    public void preUpdate() {
        updateStatus();
    }

    private void updateStatus() {
        if (quantity == 0 && status != ProductStatus.SOLD_OUT) {
            log.info("Product ID {} quantityInStock is 0, setting status to SOLD_OUT", id);
            status = ProductStatus.SOLD_OUT;
        } else if (quantity > 0 && status == ProductStatus.SOLD_OUT) {
            log.info("Product ID {} quantityInStock is {}, setting status to IN_STOCK", id, quantity);
            status = ProductStatus.IN_STOCK;
        }
    }
}
