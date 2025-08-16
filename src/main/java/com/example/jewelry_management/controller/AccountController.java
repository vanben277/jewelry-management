package com.example.jewelry_management.controller;

import com.example.jewelry_management.dto.ApiResponse;
import com.example.jewelry_management.dto.res.*;
import com.example.jewelry_management.form.*;
import com.example.jewelry_management.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@CrossOrigin(
        origins = {
                "http://127.0.0.1:5500",
                "http://192.168.1.110:5500"
        }
)
public class AccountController {
    private final AccountService accountService;

    @PostMapping(value = "register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> register(@Valid @ModelAttribute RegisterForm form) {
        RegisterResponse response = accountService.register(form);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Tạo tài khoản thành công!", response));
    }

    @PostMapping("login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginForm form) {
        LoginResponse response = accountService.login(form);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Đăng nhập thành công!", response));
    }

    @PutMapping(value = "account/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> updateInfo(@PathVariable Integer id, @Valid @ModelAttribute UpdateInfoAccountForm form) {
        UpdateInfoAccountResponse response = accountService.updateInfo(id, form);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Cập nhật thông tin thành công!", response));
    }

    @GetMapping("account/role")
    public ResponseEntity<ApiResponse> getAllAccountRole() {
        List<String> response = accountService.getAllAccountRole();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công!", response));
    }

    @GetMapping("account/gender")
    public ResponseEntity<ApiResponse> getAllAccountGender() {
        List<String> response = accountService.getAllAccountGender();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công!", response));
    }

    @GetMapping("account/status")
    public ResponseEntity<ApiResponse> getAllAccountStatus() {
        List<String> response = accountService.getAllAccountStatus();
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công!", response));
    }

    @PostMapping("forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordForm form) {
        accountService.forgotPassword(form);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Mã otp đã được gửi về email của bạn!", null));
    }

    @PutMapping("reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordForm form) {
        accountService.resetPassword(form);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Đổi mật khẩu thành công!", null));
    }

    @PutMapping("account/role/{id}")
    public ResponseEntity<ApiResponse> updateRole(@PathVariable Integer id, @Valid @RequestBody UpdateAccountRole form) {
        accountService.updateRole(id, form);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Phân quyền thành công", null));
    }

    @PutMapping("account/status/{id}")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable Integer id, @Valid @RequestBody UpdateAccountStatus form) {
        accountService.updateStatus(id, form);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Cập nhật trạng thái thành công", null));
    }

    @GetMapping("account/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer id) {
        AccountResponse response = accountService.getById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công!", response));
    }

    @PutMapping("account/change-password")
    public ResponseEntity<ApiResponse> changePassword(@Valid @RequestBody ChangePassword form) {
        accountService.changePassword(form);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Đổi mật khẩu thành công", null));
    }

    @GetMapping("account/filter")
    public ResponseEntity<ApiResponse> filter(AccountFilterForm accountFilterForm) {
        Page<AccountResponseFull> accountResponsePage = accountService.filter(accountFilterForm);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiResponse("Thành công", accountResponsePage));

    }
}
