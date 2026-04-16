package com.example.demo_mini_10.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class StudentIdValidator implements ConstraintValidator<ValidStudentId, String> {
    private String pattern;
    
    @Override
    public void initialize(ValidStudentId constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // null là hợp lệ (được handle bởi @NotBlank nếu cần)
        if (value == null || value.isEmpty()) {
            return true;
        }
        
        // Kiểm tra xem value có match pattern không
        return Pattern.matches(pattern, value);
    }
}
