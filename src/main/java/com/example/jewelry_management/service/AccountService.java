package com.example.jewelry_management.service;

import com.example.jewelry_management.dto.res.*;
import com.example.jewelry_management.form.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AccountService {
    RegisterResponse register(@Valid RegisterForm form);

    LoginResponse login(@Valid LoginForm form);

    UpdateInfoAccountResponse updateInfo(Integer id, @Valid UpdateInfoAccountForm form);

    List<String> getAllAccountRole();

    List<String> getAllAccountGender();

    List<String> getAllAccountStatus();

    void forgotPassword(@Valid ForgotPasswordForm form);

    void resetPassword(@Valid ResetPasswordForm form);

    void updateRole(Integer id, @Valid UpdateAccountRole form);

    void updateStatus(Integer id, @Valid UpdateAccountStatus form);

    AccountResponse getById(Integer id);

    void changePassword(@Valid ChangePassword form);

    Page<AccountResponseFull> filter(AccountFilterForm accountFilterForm);
}
