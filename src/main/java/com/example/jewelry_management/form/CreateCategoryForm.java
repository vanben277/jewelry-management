package com.example.jewelry_management.form;

import com.example.jewelry_management.dto.base.BaseDtoCategory;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class CreateCategoryForm implements BaseDtoCategory {
    @NotBlank(message = "Trường tên thể loại không được bỏ trống")
    private String name;

    @NotBlank(message = "Trường mô tả không được bỏ trống")
    private String description;

    private Integer parentId;

    private MultipartFile bannerUrl;

    @AssertTrue(message = "Ảnh không được để trống")
    public boolean isBannerUrlValid() {
        return bannerUrl != null && !bannerUrl.isEmpty();
    }
}
