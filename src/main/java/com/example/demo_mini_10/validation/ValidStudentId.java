package com.example.demo_mini_10.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = StudentIdValidator.class)
@Documented
public @interface ValidStudentId {
    String message() default "Mã sinh viên phải bắt đầu bằng 2 chữ cái theo sau là các chữ số (ví dụ: SV123456)";
    
    Class<?>[] groups() default {};
    
    Class<? extends Payload>[] payload() default {};
    
    // Pattern: 2 letters followed by digits (e.g., SV123456, AB999)
    String pattern() default "^[A-Za-z]{2}\\d+$";
}
