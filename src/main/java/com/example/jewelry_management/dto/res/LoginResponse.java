package com.example.jewelry_management.dto.res;

import com.example.jewelry_management.enums.AccountGender;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.AccountStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class LoginResponse {
    private Integer id;
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private AccountRole role;
    private AccountGender gender;
    private String avatar;
    private String phone;
    private String address;
    private String email;
}
