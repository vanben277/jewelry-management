package com.example.jewelry_management.form;

import com.example.jewelry_management.enums.AccountGender;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AccountFilterForm {
    private String phone;
    private AccountRole role;
    private AccountGender gender;
    private AccountStatus status;

    private Integer pageSize = 10;
    private Integer pageNumber = 0;
}
