package com.example.jewelry_management.model;

import com.example.jewelry_management.enums.AccountGender;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String userName;

    @Column(name = "firstname", nullable = false, length = 100)
    private String firstName;

    @Column(name = "lastname", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "ENUM('MALE','FEMALE','OTHER')")
    private AccountGender gender;

    @Column(name = "avatar", columnDefinition = "TEXT")
    private String avatar;

    @Column(name = "phone", unique = true, length = 20)
    private String phone;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private AccountRole role = AccountRole.USER;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status = AccountStatus.ACTIVE;

    @Column(name = "otp_sku", length = 10)
    private String otpSku;

    @Column(name = "expiry_otp")
    private LocalDateTime expiryOtp;

    @Column(name = "last_login_at")
    private LocalDateTime lastLoginAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateLastLogin() {
        this.lastLoginAt = LocalDateTime.now();
    }
}