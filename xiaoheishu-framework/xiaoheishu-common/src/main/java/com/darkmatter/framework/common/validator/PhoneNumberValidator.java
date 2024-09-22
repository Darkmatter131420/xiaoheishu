package com.darkmatter.framework.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * @author DarkMatter
 * @version 1.0.0
 * @date 2024/7/31 20点50分
 * @description: TODO
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {
    @Override
    public void initialize(PhoneNumber constraintAnnotation) {
        // 这里进行初始化操作
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        // 校验逻辑：正则表达式判断手机号是否为11位数字
        return phoneNumber != null && phoneNumber.matches("\\d{11}");
    }
}
