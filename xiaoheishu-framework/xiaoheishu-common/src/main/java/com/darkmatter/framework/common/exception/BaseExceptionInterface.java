package com.darkmatter.framework.common.exception;

public interface BaseExceptionInterface {

    //获取异常状态码
    String getErrorCode();

    //获取异常信息
    String getErrorMessage();
}
