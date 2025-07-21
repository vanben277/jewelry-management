package com.example.jewelry_management.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotBlank(message = "Tài khoản không được bỏ trống")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]{4,30}$",
            message = "Tài khoản phải có 4-30 ký tự, chỉ chứa chữ, số, dấu chấm, gạch dưới và gạch ngang"
    )
    private String userName;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "Mật khẩu phải có 8-16 ký tự, chứa ít nhất: 1 chữ thường, 1 chữ hoa, 1 số, 1 ký tự đặc biệt (@$!%*?&)"
    )
    private String password;
}
