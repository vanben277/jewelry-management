package com.example.jewelry_management.dto.res;

import com.example.jewelry_management.enums.AccountGender;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private Integer id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private AccountRole role;
    private LocalDate dateOfBirth;
    private AccountGender gender;
    private String avatar;
    private String phone;
    private String address;
    private String email;
    private AccountStatus status;

}
