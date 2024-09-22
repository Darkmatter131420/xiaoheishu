package com.darkmatter.xiaoheishu.auth.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.darkmatter.framework.common.exception.BizException;
import com.darkmatter.framework.common.response.Response;
import com.darkmatter.xiaoheishu.auth.constant.RedisKeyConstants;
import com.darkmatter.xiaoheishu.auth.enums.ResponseCodeEnum;
import com.darkmatter.xiaoheishu.auth.model.vo.verificationcode.SendVerificationCodeReqVo;
import com.darkmatter.xiaoheishu.auth.service.VerificationCodeService;
import com.darkmatter.xiaoheishu.auth.sms.AliyunSmsHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class VerificationCodeServiceImpl implements VerificationCodeService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Resource
    private AliyunSmsHelper aliyunSmsHelper;
    /**
     * 发送短信验证码
     *
     * @param sendVerificationCodeReqVo
     * @return
     */
    @Override
    public Response<?> send(SendVerificationCodeReqVo sendVerificationCodeReqVo) {
        // 手机号
        String phone = sendVerificationCodeReqVo.getPhone();

        // 构建验证码 redis key
        String key = RedisKeyConstants.buildVerificationCodeKey(phone);

        // 判断是否已发送验证码
        boolean isSent = redisTemplate.hasKey(key);
        if (isSent) {
            // 若之前发送的验证码未过期，则提示发送频繁
            throw new BizException(ResponseCodeEnum.VERIFICATION_CODE_SEND_FREQUENTLY);
        }

        // 生成6位随机验证码
        String verificationCode = RandomUtil.randomNumbers(6);

        //todo: 调用第三方短信服务发送验证码
        log.info("==> 手机号: {}, 已生成验证码：【{}】", phone, verificationCode);

        threadPoolTaskExecutor.submit(() -> {
            String signName = "阿里云短信测试";
            String templateCode = "SMS_154950909";
            String templateParam = "{\"code\":\"" + verificationCode + "\"}";
            aliyunSmsHelper.sendMessage(signName, templateCode, phone, templateParam);
        });

//        log.info("==> 手机号：{}，已发送验证码：【{}】", phone, verificationCode);

        // 存储验证码到redis，并设置过期时间为3分钟
        redisTemplate.opsForValue().set(key, verificationCode, 3, TimeUnit.MINUTES);

        return Response.success();
    }
}
