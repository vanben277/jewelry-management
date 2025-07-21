package com.example.jewelry_management.form;

import com.example.jewelry_management.dto.base.BaseDtoCategory;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateCategoryForm implements BaseDtoCategory {
    @NotBlank(message = "Trường tên thể loại không được bỏ trống")
    private String name;

    @NotBlank(message = "Trường mô tả không được bỏ trống")
    private String description;

    private LocalDateTime updateAt;

    private Integer parentId;

    private MultipartFile bannerUrl;
}
