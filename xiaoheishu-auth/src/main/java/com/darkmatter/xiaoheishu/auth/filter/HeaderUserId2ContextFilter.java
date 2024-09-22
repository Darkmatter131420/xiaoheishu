package com.darkmatter.xiaoheishu.auth.filter;

import com.darkmatter.framework.common.constant.GlobalConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.darkmatter.framework.common.constant.GlobalConstants.USER_ID;

@Component
@Slf4j
public class HeaderUserId2ContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 从请求头获取用户 ID
        String userId = request.getHeader(GlobalConstants.USER_ID);

        log.info("## HeaderUserId2ContextFilter 用户 ID: {}", userId);

        // 判断请求头中是否存在用户 ID
        if (StringUtils.isBlank(userId)) {
            // 若为空， 则直接放行
            filterChain.doFilter(request, response);
            return;
        }

        // 如果Header中存在userId，则将userId设置到 ThreadLocal 中
        log.info(" ====== 设置用户 ID 到 ThreadLocal 中， 用户 ID: {}", userId);
        LoginUserContextHolder.setUserId(userId);

        try {
            // 将请求和响应传递给过滤链中的下一个过滤器
            filterChain.doFilter(request, response);
        } finally {
            // 释放资源
            LoginUserContextHolder.remove();
            log.info(" ====== 删除 ThreadLocal, userId: {}", userId);
        }

    }
}
