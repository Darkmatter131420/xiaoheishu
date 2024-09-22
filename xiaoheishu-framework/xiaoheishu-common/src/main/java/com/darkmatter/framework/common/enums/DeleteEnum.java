package com.darkmatter.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author DarkMatter
 * @date 2024/8/4 10:33
 * @version 1.0.0
 * @description 删除枚举
 */
@Getter
@AllArgsConstructor
public enum DeleteEnum {

    YES(true),
    NO(false);

    private final Boolean value;
}
