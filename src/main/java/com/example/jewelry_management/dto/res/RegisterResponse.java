package com.example.jewelry_management.dto.res;

import com.example.jewelry_management.enums.AccountGender;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterResponse {
    private Integer id;
    private String userName;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private AccountGender gender;
    private String avatar;
    private String phone;
    private String address;
    private String email;
    private String password;
}
