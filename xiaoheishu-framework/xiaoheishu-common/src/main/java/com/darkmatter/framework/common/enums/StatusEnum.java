package com.darkmatter.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author DarkMatter
 * @date 2024/8/4 14:54
 * @version 1.0.0
 * @description 状态枚举
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    // 启用
    ENABLE(0),
    // 禁用
    DISABLE(1);

    private final Integer value;
}
