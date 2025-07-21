package com.example.jewelry_management.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.logging.log4j.core.config.plugins.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageForm {
    @NotBlank(message = "link ảnh không được bỏ trống")
    private String imageUrl;

    private Boolean isPrimary = false;

}
