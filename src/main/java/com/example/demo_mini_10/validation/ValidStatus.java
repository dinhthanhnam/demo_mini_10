package com.example.demo_mini_10.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StatusValidator.class)
@Documented
public @interface ValidStatus {
    String message() default "Trạng thái phải là PENDING, APPROVED hoặc REJECTED";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    // Cho phép custom giá trị hợp lệ
    String[] allowedValues() default {"PENDING", "APPROVED", "REJECTED"};
}
