package com.darkmatter.xiaoheishu.gateway.enums;

import com.darkmatter.framework.common.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    // ---------------通用异常状态码---------------
    SYSTEM_ERROR("500", "系统繁忙，请稍后再试"),
    UNAUTHORIZED("401", "权限不足"),

    // ---------------业务异常状态码---------------
    ;

    private final String errorCode;

    private final String errorMessage;
}
