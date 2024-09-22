package com.darkmatter.xiaoheishu.auth.sms;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teautil.models.RuntimeOptions;
import com.darkmatter.framework.common.util.JsonUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author: DarkMatter
 * @date: 2024年7月31日18点19分
 * @version 1.0.0
 * @Description: 阿里云短信发送工具类
 */
@Component
@Slf4j
public class AliyunSmsHelper {
    @Resource
    private Client client;

    /**
     * 发送短信
     * @param signName 短信签名
     * @param templateCode 短信模板
     * @param phone 手机号
     * @param templateParam 模板参数
     * @return
     */
    public Boolean sendMessage(String signName, String templateCode, String phone, String templateParam) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setPhoneNumbers(phone)
                .setSignName(signName)
                .setTemplateCode(templateCode)
                .setTemplateParam(templateParam);
        RuntimeOptions runtime = new RuntimeOptions();

        try {
            log.info("==> 开始短信发送,phone:{},signName:{},templateCode:{},templateParam:{}", phone, signName, templateCode, templateParam);

            // 发送短信
            SendSmsResponse response = client.sendSmsWithOptions(sendSmsRequest, runtime);

            log.info("==> 短信发送成功,response：{}", JsonUtils.toJsonString(response));
            return true;
        }catch (Exception error){
            log.error("==> 短信发送失败", error);
            return false;
        }
    }
}
