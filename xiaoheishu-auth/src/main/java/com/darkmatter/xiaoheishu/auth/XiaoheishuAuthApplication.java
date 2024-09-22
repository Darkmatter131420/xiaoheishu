package com.darkmatter.xiaoheishu.auth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.darkmatter.xiaoheishu.auth.domain.mapper")
@SpringBootApplication
public class XiaoheishuAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(XiaoheishuAuthApplication.class, args);
    }

}
