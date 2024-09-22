package com.darkmatter.xiaoheishu.gateway.filter;

import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author DarkMatter
 * @Date 2024年9月10日 17点30分
 * @Version v1.0.0
 * @Description 转发请求时，添加用户 ID 到请求头， 透传到下游服务
 */
@Component
@Slf4j
public class AddUserId2HeaderFilter implements GlobalFilter {

    // 请求头中，用户 ID 的键
    private static final String HEADER_USER_ID = "userId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("=========> TokenConvertFilter");

        // 用户 ID
        Long userId = null;
        try{
            // 获取当前登录用户的 ID
            userId = StpUtil.getLoginIdAsLong();
        }catch (Exception e){
            return chain.filter(exchange);
        }
        log.info("## 当前登录的用户 Id: {}", userId);
        Long finalUserId = userId;
        ServerWebExchange newExchange = exchange.mutate()
                .request(builder -> builder.header(HEADER_USER_ID, String.valueOf(finalUserId)))
                .build();

        return chain.filter(newExchange);
    }
}
