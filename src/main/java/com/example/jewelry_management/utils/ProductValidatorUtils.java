package com.example.jewelry_management.utils;

import com.example.jewelry_management.enums.GoldType;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.form.ProductImageForm;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ProductValidatorUtils {
    public void validateGoldType(String goldType) {
        if (goldType == null) {
            return;
        }

        if (goldType.trim().isEmpty()) {
            throw new IllegalArgumentException("Loại vàng không được để trống. Vui lòng chọn: GOLD_10K, GOLD_14K, GOLD_18K, GOLD_24K");
        }

        try {
            GoldType.valueOf(goldType.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Loại vàng không hợp lệ: '" + goldType + "'. Vui lòng chọn: GOLD_10K, GOLD_14K, GOLD_18K, GOLD_24K");
        }
    }

    public void validatorImages(List<ProductImageForm> images) {
        /* chưa xử lý được logic
        if (images == null || images.isEmpty()) {
            throw new BusinessException("Sản phẩm phải có ít nhất 1 ảnh", ErrorCodeConstant.INVALID_INPUT);
        }
        */
        Set<String> imageUrls = new HashSet<>();
        for (ProductImageForm form : images) {
            if (!imageUrls.add(form.getImageUrl())) {
                throw new BusinessException("Đường dẫn ảnh không được trùng nhau", ErrorCodeConstant.INVALID_INPUT);
            }
        }
    }
}
