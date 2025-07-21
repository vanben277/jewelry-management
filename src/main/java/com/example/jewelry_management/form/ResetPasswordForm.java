package com.example.jewelry_management.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordForm {

    @NotBlank(message = "Mã otp không được bỏ trống")
    private String otpSku;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "Mật khẩu phải có 8-16 ký tự, chứa ít nhất: 1 chữ thường, 1 chữ hoa, 1 số, 1 ký tự đặc biệt (@$!%*?&)"
    )
    private String newPassword;
}
