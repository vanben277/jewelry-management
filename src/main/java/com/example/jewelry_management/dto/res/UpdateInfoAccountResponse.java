package com.example.jewelry_management.dto.res;

import com.example.jewelry_management.enums.AccountGender;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class UpdateInfoAccountResponse {
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private AccountGender gender;
    private String avatar;
    private String phone;
    private String address;
    private String email;
}
