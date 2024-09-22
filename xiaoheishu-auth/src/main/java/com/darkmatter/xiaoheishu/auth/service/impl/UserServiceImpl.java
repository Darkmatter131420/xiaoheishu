package com.darkmatter.xiaoheishu.auth.service.impl;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.darkmatter.framework.common.enums.DeleteEnum;
import com.darkmatter.framework.common.enums.StatusEnum;
import com.darkmatter.framework.common.response.Response;
import com.darkmatter.framework.common.util.JsonUtils;
import com.darkmatter.xiaoheishu.auth.constant.RedisKeyConstants;
import com.darkmatter.xiaoheishu.auth.constant.RoleConstants;
import com.darkmatter.xiaoheishu.auth.domain.dataobject.RoleDO;
import com.darkmatter.xiaoheishu.auth.domain.dataobject.UserDO;
import com.darkmatter.xiaoheishu.auth.domain.dataobject.UserRoleDO;
import com.darkmatter.xiaoheishu.auth.domain.mapper.RoleDOMapper;
import com.darkmatter.xiaoheishu.auth.domain.mapper.UserDOMapper;
import com.darkmatter.xiaoheishu.auth.domain.mapper.UserRoleDOMapper;
import com.darkmatter.xiaoheishu.auth.enums.LoginTypeEnum;
import com.darkmatter.xiaoheishu.auth.enums.ResponseCodeEnum;
import com.darkmatter.xiaoheishu.auth.filter.LoginUserContextHolder;
import com.darkmatter.xiaoheishu.auth.model.vo.user.UserLoginReqVo;
import com.darkmatter.xiaoheishu.auth.service.UserService;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author DarkMatter
 * @date 2024/8/3 22:12
 * @version v1.0.0
 * @description TODO
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Resource
    private UserDOMapper userDOMapper;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private UserRoleDOMapper userRoleDOMapper;

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private RoleDOMapper roleDOMapper;

    @Resource(name = "taskExecutor")
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
    /**
     * 登录与注册
     *
     * @param userLoginReqVo
     * @return
     */
    @Override
    public Response<String> loginAndRegister(UserLoginReqVo userLoginReqVo) {
        String phone = userLoginReqVo.getPhone();
        Integer type = userLoginReqVo.getType();

        LoginTypeEnum loginTypeEnum = LoginTypeEnum.valueOf(type);

        Long userId = null;

        // 判断登录类型
        switch (loginTypeEnum) {
            case VERIFICATION_CODE:
                String verificationCode = userLoginReqVo.getCode();
                // 校验入参验证码是否为空
//                if (StringUtils.isBlank(verificationCode)) {
//                    return Response.fail(ResponseCodeEnum.PARAM_ERROR.getErrorCode(),"验证码不能为空");
//                }
                Preconditions.checkArgument(StringUtils.isNotBlank(verificationCode), "验证码不能为空");

                // 构建验证码 RedisKey
                String key = RedisKeyConstants.buildVerificationCodeKey(phone);
                // 查询存储在 Redis 中该用户的登录验证码
                String sentCode = (String) redisTemplate.opsForValue().get(key);

                // 判断用户提交的验证码，与 redis 中的验证码是否一致
                if (!verificationCode.equals(sentCode)) {
                    return Response.fail(ResponseCodeEnum.VERIFICATION_CODE_ERROR);
                }

                // 通过手机号查询记录
                UserDO userDO = userDOMapper.selectByPhone(phone);

                log.info("用户是否注册，phone: {}, userDO: {}", phone, JsonUtils.toJsonString(userDO));

                // 判断用户是否注册
                if (Objects.isNull(userDO)) {
                    // 若此用户还没有注册，系统自动注册该用户
                    userId = registerUser(phone);
                }else {
                    // 若此用户已经注册，直接登录
                    userId = userDO.getId();
                }

                break;
            case PASSWORD:
                // 密码登录
                // todo
                break;
            default:
                break;
        }

        // saToken 登录用户， 入参为用户ID
        StpUtil.login(userId);

        // 获取 Token 令牌
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        // 返回 Token 令牌
        return Response.success(tokenInfo.tokenValue);
    }

    /**
     * 退出登录
     *
     * @return
     */
    @Override
    public Response<?> logout() {
        Long userId = LoginUserContextHolder.getUserId();

        log.info("==> 用户退出登录，userID: {}", userId);

        threadPoolTaskExecutor.submit(() -> {
            Long userId2 = LoginUserContextHolder.getUserId();
            log.info("==> 异步线程中获取 userId: {}", userId2);
        });
        // 退出登录 (指定用户 ID)
        StpUtil.logout(userId);

        return Response.success();
    }

    /**
     * 系统自动注册用户
     * @param phone
     * @return
     */
    public Long registerUser(String phone) {
        return transactionTemplate.execute(status -> {
            try {
                // 获取全局自增的小黑书 ID
                Long xiaoheishuId = redisTemplate.opsForValue().increment(RedisKeyConstants.XIAOHEISHU_ID_GENERATOR_KEY);

                UserDO userDO = UserDO.builder()
                        .phone(phone)
                        .xiaoheishuId(String.valueOf(xiaoheishuId)) // 自动生成小黑书号 ID
                        .nickname("小黑薯" + xiaoheishuId) // 自动生成昵称
                        .status(StatusEnum.ENABLE.getValue()) // 状态 启用
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeleteEnum.NO.getValue()) // 逻辑删除
                        .build();

                // 添加入库
                userDOMapper.insert(userDO);

                // 获取刚刚添加入库的用户 ID
                Long userId = userDO.getId();

                // 给该用户分配一个默认角色
                UserRoleDO userRoleDO = UserRoleDO.builder()
                        .userId(userId)
                        .roleId(RoleConstants.COMMON_USER_ROLE_ID) // 默认角色 ID
                        .createTime(LocalDateTime.now())
                        .updateTime(LocalDateTime.now())
                        .isDeleted(DeleteEnum.NO.getValue()) // 逻辑删除
                        .build();
                userRoleDOMapper.insert(userRoleDO);

                RoleDO roleDO = roleDOMapper.selectByPrimaryKey(RoleConstants.COMMON_USER_ROLE_ID);

                // 将该用户的角色 ID 存入 Redis, 指定初始容量为1，这样可以减少在扩容时的性能开销
                List<String> roles = new ArrayList<>(1);
                roles.add(roleDO.getRoleKey());

                String userRolesKey = RedisKeyConstants.buildUserRoleKey(userId);
                redisTemplate.opsForValue().set(userRolesKey, JsonUtils.toJsonString(roles));

                return userId;
            }
            catch (Exception e) {
                status.setRollbackOnly();
                log.error("==> 系统注册用户异常：", e);
                return null;
            }
        });
    }
}
