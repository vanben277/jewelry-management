package com.example.jewelry_management.repository;

import com.example.jewelry_management.model.Account;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer>, JpaSpecificationExecutor<Account> {
    Optional<Account> findByUserName(String username);

    Optional<Account> findByEmail(@NotBlank(message = "Email không được bỏ trống") @Email(message = "Email không hợp lệ") String email);

    @Query(value = "select * from account where username = ?1", nativeQuery = true)
    Optional<Account> findByUserName1(@NotBlank(message = "Tài khoản không được bỏ trống") @Pattern(
            regexp = "^[a-zA-Z0-9._-]{4,30}$",
            message = "Tài khoản phải có 4-30 ký tự, chỉ chứa chữ, số, dấu chấm, gạch dưới và gạch ngang"
    ) String userName);

    Optional<Account> findByPhone(@Pattern(
            regexp = "^(0[0-9]{9}|\\+84[0-9]{9})$",
            message = "Số điện thoại không hợp lệ (bắt đầu bằng 0 hoặc +84 và có 10 chữ số)"
    ) String phone);

    Optional<Account> findByOtpSku(@NotBlank(message = "Mã otp không được bỏ trống") String otpSku);
}
