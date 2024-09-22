package com.darkmatter.xiaoheishu.auth.controller;

import com.darkmatter.framework.biz.operationlog.aspect.ApiOperationLog;
import com.darkmatter.framework.common.response.Response;
import com.darkmatter.xiaoheishu.auth.model.vo.user.UserLoginReqVo;
import com.darkmatter.xiaoheishu.auth.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @Author DarkMatter
 * @Date 2024/8/3 22:12
 * @Version v1.0.0
 * @Description TODO
 **/
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/login")
    @ApiOperationLog(description = "用户登录/注册")
    public Response<String> loginAndRegister(@Validated @RequestBody UserLoginReqVo userLoginReqVo) {
        return userService.loginAndRegister(userLoginReqVo);
    }

    @PostMapping("/logout")
    @ApiOperationLog(description = "账号登出")
    public Response<?> logout() {

        return userService.logout();
    }

}
