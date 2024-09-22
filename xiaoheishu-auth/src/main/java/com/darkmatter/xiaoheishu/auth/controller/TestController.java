package com.darkmatter.xiaoheishu.auth.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.darkmatter.xiaoheishu.auth.alarm.AlarmInterface;
import jakarta.annotation.Resource;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class TestController {

    @Resource
    private AlarmInterface alarm;

    @NacosValue(value = "${rate-limit.api.limit}", autoRefreshed = true)
    private Integer limit;

    @GetMapping("/test")
    public String test() {
        return "当前限流阈值为：" + limit;
    }

    @GetMapping("/alarm")
    public String sendAlarm() {
        alarm.send("系统出错辣！");
        return "alarm success";
    }
}
