package com.darkmatter.xiaoheishu.auth.service;

import com.darkmatter.framework.common.response.Response;
import com.darkmatter.xiaoheishu.auth.model.vo.user.UserLoginReqVo;

/**
 * @author DarkMatter
 * @date 2024/8/3 22:12
 * @version v1.0.0
 * @description TODO
 */
public interface UserService {

    /**
     * 登录与注册
     * @param userLoginReqVo
     * @return
     */
    Response<String> loginAndRegister(UserLoginReqVo userLoginReqVo);

    /**
     * 退出登录
     * @return
     */
    Response<?> logout();
}
