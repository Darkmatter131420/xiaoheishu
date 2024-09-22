package com.darkmatter.framework.common.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author DarkMatter
 * @version 1.0.0
 * @date 2024/7/31 21点12分
 * @description: 自定义手机号校验注解
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneNumberValidator.class)
public @interface PhoneNumber {

    String message() default "手机号格式不正确, 需为11位数字";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
