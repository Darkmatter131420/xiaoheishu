package com.darkmatter.xiaoheishu.gateway.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenException;
import com.darkmatter.framework.common.response.Response;
import com.darkmatter.xiaoheishu.gateway.enums.ResponseCodeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
@Slf4j
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        // 获取响应对象
        ServerHttpResponse response = exchange.getResponse();

        log.info("==> 全局异常信息捕获：", ex);

        // 响参
        Response<?> result;
        // 根据捕获的异常类型，设置不同的响应状态码和响应信息
        if(ex instanceof NotLoginException){
            // 权限认证失败时，设置 401 状态码
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 构建响应结果
            result = Response.fail(ResponseCodeEnum.UNAUTHORIZED.getErrorCode(), ex.getMessage());
        } else if (ex instanceof NotPermissionException) {
            // 权限认证失败时，设置 401 状态码
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            // 构建响应结果
            result = Response.fail(ResponseCodeEnum.UNAUTHORIZED.getErrorCode(), ResponseCodeEnum.UNAUTHORIZED.getErrorMessage());
        } else {
            // 其他异常, 则统一提示“系统繁忙”错误
            result = Response.fail(ResponseCodeEnum.SYSTEM_ERROR);
        }

        // 设置响应头的内容类型为 application/json;charset=UTF-8,表示响应体为 JSON 格式，并且编码为 UTF-8
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
        // 设置 body 响应体
        return response.writeWith(Mono.fromSupplier(() -> { // 使用 Mono.fromSupplier 创建响应体
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                // 使用 ObjectMapper 将 result 对象转换为 JSON 字符串
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(result));
            } catch (Exception e) {
                // 如果转换过程中出现异常，则返回空字符串
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }
}
