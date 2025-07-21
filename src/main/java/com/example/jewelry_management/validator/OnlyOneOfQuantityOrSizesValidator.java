package com.example.jewelry_management.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OnlyOneOfQuantityOrSizesValidator implements ConstraintValidator<OnlyOneOfQuantityOrSizes, QuantitySizeValidationTarget> {

    @Override
    public boolean isValid(QuantitySizeValidationTarget form, ConstraintValidatorContext context) {
        boolean hasQuantity = form.getQuantity() != null;
        boolean hasSizes = form.getSizes() != null && !form.getSizes().isEmpty();

        boolean valid = hasQuantity ^ hasSizes;

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Chỉ được truyền quantity hoặc sizes, không được truyền cả 2, hãy bỏ 1 trong 2 quantity hoặc sizes")
                    .addConstraintViolation();
        }

        return valid;
    }
}
