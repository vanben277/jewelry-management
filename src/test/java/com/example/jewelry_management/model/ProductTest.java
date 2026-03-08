package com.example.jewelry_management.model;

import com.example.jewelry_management.enums.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ProductTest {
    @Test
    @DisplayName("Nên tính tổng số lượng từ các size và cập nhật trạng thái IN_STOCK")
    void shouldUpdateStatusToInStockWhenTotalQuantityGreaterThanZero() {
        Product product = new Product();
        List<ProductSize> sizes = new ArrayList<>();

        // Thêm size 10 có 5 cái
        ProductSize s1 = new ProductSize();
        s1.setQuantity(5);
        sizes.add(s1);

        // Thêm size 11 có 10 cái
        ProductSize s2 = new ProductSize();
        s2.setQuantity(10);
        sizes.add(s2);

        product.setSizes(sizes);

        // 2. Thực thi hàm cần test (Act)
        product.updateStatus();

        // 3. Kiểm tra kết quả (Assert)
        assertEquals(15, product.getQuantity(), "Tổng số lượng phải bằng 15 (5 + 10)");
        assertEquals(ProductStatus.IN_STOCK, product.getStatus(), "Trạng thái phải là IN_STOCK");
    }

    @Test
    @DisplayName("Nên cập nhật trạng thái SOLD_OUT khi tổng số lượng bằng 0")
    void shouldUpdateStatusToSoldOutWhenTotalQuantityIsZero() {
        // 1. Chuẩn bị dữ liệu (Arrange)
        Product product = new Product();
        List<ProductSize> sizes = new ArrayList<>();

        ProductSize s1 = new ProductSize();
        s1.setQuantity(0);
        sizes.add(s1);

        product.setSizes(sizes);

        // 2. Thực thi (Act)
        product.updateStatus();

        // 3. Kiểm tra (Assert)
        assertEquals(0, product.getQuantity(), "Tổng số lượng phải bằng 0");
        assertEquals(ProductStatus.SOLD_OUT, product.getStatus(), "Trạng thái phải là SOLD_OUT");
    }
}
