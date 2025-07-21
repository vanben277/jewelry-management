package com.example.jewelry_management.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountRole {
    @NotBlank(message = "Phân quyền không được bỏ trống")
    private String role;
}
