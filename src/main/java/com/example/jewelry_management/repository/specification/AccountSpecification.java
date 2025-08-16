package com.example.jewelry_management.repository.specification;


import com.example.jewelry_management.enums.AccountGender;
import com.example.jewelry_management.enums.AccountRole;
import com.example.jewelry_management.enums.AccountStatus;
import com.example.jewelry_management.model.Account;
import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification {
    public static Specification<Account> phoneConstant(String phone) {
        return ((root, query, criteriaBuilder) -> {
            if (phone == null || phone.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("phone")), "%" + phone + "%");
        });
    }

    public static Specification<Account> roleEquals(AccountRole role) {
        return ((root, query, criteriaBuilder) -> {
            if (role == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("role"), role);
        });
    }

    public static Specification<Account> genderEquals(AccountGender gender) {
        return ((root, query, criteriaBuilder) -> {
            if (gender == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("gender"), gender);
        });
    }

    public static Specification<Account> statusEquals(AccountStatus status) {
        return ((root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        });
    }
}
