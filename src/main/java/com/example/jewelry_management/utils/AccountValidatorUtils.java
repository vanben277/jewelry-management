package com.example.jewelry_management.utils;

import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.exception.BusinessException;
import com.example.jewelry_management.exception.ErrorCodeConstant;
import com.example.jewelry_management.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountValidatorUtils {
    public void validatorAccountStatus(Account account) {
        switch (account.getStatus()) {
            case INACTIVE ->
                    throw new BusinessException("Tài khoản đã bị vô hiệu hóa. Vui lòng liên hệ quản trị viên!", ErrorCodeConstant.ACCOUNT_DISABLED);
            case BANNED ->
                    throw new BusinessException("Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên!", ErrorCodeConstant.ACCOUNT_LOCKED);
            case PENDING ->
                    throw new BusinessException("Tài khoản chưa được xác thực. Vui lòng kiểm tra email!", ErrorCodeConstant.ACCOUNT_PENDING);
            case ACTIVE -> {
            }
            default ->
                    throw new BusinessException("Trạng thái tài khoản không hợp lệ!", ErrorCodeConstant.ACCOUNT_DISABLED);
        }
    }

    public void validateAccountRole(String role) {
        try {
            AccountRole.valueOf(role.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Phân quyền không hợp lệ: '" + role + "'. Vui lòng chọn: ADMIN, STAFF, USER");
        }
    }
}
