package com.example.jewelry_management.form;

import com.example.jewelry_management.enums.AccountGender;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Getter
@Setter
public class RegisterForm {
    @NotBlank(message = "Tài khoản không được bỏ trống")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]{4,30}$",
            message = "Tài khoản phải có 4-30 ký tự, chỉ chứa chữ, số, dấu chấm, gạch dưới và gạch ngang"
    )
    private String userName;

    @NotBlank(message = "Họ không được bỏ trống")
    @Size(max = 50, message = "Họ không được dài quá 50 ký tự")
    private String firstName;

    @NotBlank(message = "Tên không được bỏ trống")
    @Size(max = 50, message = "Tên không được dài quá 50 ký tự")
    private String lastName;

    @Past(message = "Ngày sinh phải là ngày trong quá khứ")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    private AccountGender gender;

    private MultipartFile avatar;

    @Pattern(
            regexp = "^(0[0-9]{9}|\\+84[0-9]{9})$",
            message = "Số điện thoại không hợp lệ (bắt đầu bằng 0 hoặc +84 và có 10 chữ số)"
    )
    private String phone;

    @Size(max = 255, message = "Địa chỉ không được dài quá 255 ký tự")
    private String address;

    @NotBlank(message = "Email không được bỏ trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Mật khẩu không được bỏ trống")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,16}$",
            message = "Mật khẩu phải có 8-16 ký tự, chứa ít nhất: 1 chữ thường, 1 chữ hoa, 1 số, 1 ký tự đặc biệt (@$!%*?&)"
    )
    private String password;
}
