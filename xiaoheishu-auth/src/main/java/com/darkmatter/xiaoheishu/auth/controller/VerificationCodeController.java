package com.darkmatter.xiaoheishu.auth.controller;


import com.darkmatter.framework.biz.operationlog.aspect.ApiOperationLog;
import com.darkmatter.framework.common.response.Response;
import com.darkmatter.xiaoheishu.auth.model.vo.verificationcode.SendVerificationCodeReqVo;
import com.darkmatter.xiaoheishu.auth.service.VerificationCodeService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class VerificationCodeController {

    @Resource
    private VerificationCodeService verificationCodeService;

    @PostMapping("/verification/code/send")
    @ApiOperationLog(description = "发送短信验证码")
    public Response<?> send(@Validated @RequestBody SendVerificationCodeReqVo sendVerificationCodeReqVo) {
        return verificationCodeService.send(sendVerificationCodeReqVo);
    }
}
