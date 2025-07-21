package com.example.jewelry_management.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateAccountStatus {
    @NotBlank(message = "Trạng thái không được bỏ trống")
    private String status;
}
