package com.darkmatter.xiaoheishu.auth.service;

import com.darkmatter.framework.common.response.Response;
import com.darkmatter.xiaoheishu.auth.model.vo.verificationcode.SendVerificationCodeReqVo;

public interface VerificationCodeService {

    /**
     * 发送短信验证码
     * @param sendVerificationCodeReqVo
     * @return
     */
    Response<?> send(SendVerificationCodeReqVo sendVerificationCodeReqVo);
}
