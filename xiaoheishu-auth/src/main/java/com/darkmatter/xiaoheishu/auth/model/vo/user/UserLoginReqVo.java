package com.darkmatter.xiaoheishu.auth.model.vo.user;

import com.darkmatter.framework.common.validator.PhoneNumber;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DarkMatter
 * @date 2024/8/3 15:57
 * @version 1.0.0
 * @description 用户登录(支持密码或验证码两种方式)
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginReqVo {

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    @PhoneNumber
    private String phone;

    /**
     * 验证码
     */
    private String code;

    /**
     * 密码
     */
    private String password;

    /**
     * 登录类型
     */
    @NotNull(message = "登录类型不能为空")
    private Integer type;
}
