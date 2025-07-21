package com.example.jewelry_management.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = OnlyOneOfQuantityOrSizesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface OnlyOneOfQuantityOrSizes {
    String message() default "Chỉ được truyền một trong quantity hoặc sizes";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
