package com.example.jewelry_management.validator;

import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.exception.UnauthorizedException;
import com.example.jewelry_management.model.Account;
import com.example.jewelry_management.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountValidator {
    private final AccountRepository accountRepository;

    public void validateUniqueEmail(String email) {
        if (accountRepository.findByEmail(email).isPresent()) {
            throw new BusinessException("Email đã tồn tại trong hệ thống vui lòng thử lại!", ErrorCodeConstant.ACCOUNT_ALREADY_EXISTS_EMAIL);
        }
    }

    public void validateUniqueUserName(String userName) {
        if (accountRepository.findByEmail(userName).isPresent()) {
            throw new BusinessException("Tài khoản đã tồn tại trong hệ thống vui lòng thử lại!", ErrorCodeConstant.ACCOUNT_ALREADY_EXISTS_USER_NAME);
        }
    }

    public void validateUniquePhone(String phone) {
        if (accountRepository.findByEmail(phone).isPresent()) {
            throw new BusinessException("Số điện thoại đã tồn tại trong hệ thống vui lòng thử lại!", ErrorCodeConstant.ACCOUNT_ALREADY_EXISTS_PHONE);
        }
    }

    public Account getCurrentAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        return accountRepository.findByUserName1(userName)
                .orElseThrow(() -> new UnauthorizedException("Vui lòng đăng nhập để tiếp tục!", ErrorCodeConstant.NOT_FOUND_LOGIN));
    }
}
