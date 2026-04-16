package com.example.demo_mini_10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class StatusValidator implements ConstraintValidator<ValidStatus, String> {
    private Set<String> allowedValues;
    
    @Override
    public void initialize(ValidStatus constraintAnnotation) {
        allowedValues = new HashSet<>(Arrays.asList(constraintAnnotation.allowedValues()));
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null là hợp lệ (được handle bởi @NotNull nếu cần)
        if (value == null) {
            return true;
        }
        
        // Kiểm tra xem value có nằm trong allowedValues không
        return allowedValues.contains(value);
    }
}
