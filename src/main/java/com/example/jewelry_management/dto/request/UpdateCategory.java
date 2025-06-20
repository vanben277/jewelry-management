package com.example.jewelry_management.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateCategory implements BaseDtoCategory {
    @NotBlank(message = "Trường tên thể loại không được bỏ trống")
    private String name;

    @NotBlank(message = "Trường mô tả không được bỏ trống")
    private String description;

    private LocalDateTime updateAt;

}
