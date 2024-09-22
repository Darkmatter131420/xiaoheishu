package com.darkmatter.xiaoheishu.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * @author DarkMatter
 * @url: www.wldarkmatter.cn
 * @date 2024/8/3 22:01
 * @description: 登录类型枚举
 */
@Getter
@AllArgsConstructor
public enum LoginTypeEnum {

    // 验证码
    VERIFICATION_CODE(1),
    // 密码
    PASSWORD(2);

    private final Integer value;

    public static LoginTypeEnum valueOf(Integer code) {
        for (LoginTypeEnum loginTypeEnum : LoginTypeEnum.values()) {
            if (Objects.equals(loginTypeEnum.getValue(), code)) {
                return loginTypeEnum;
            }
        }
        return null;
    }
}
