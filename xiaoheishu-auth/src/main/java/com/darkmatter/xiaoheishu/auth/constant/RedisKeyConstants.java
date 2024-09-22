package com.darkmatter.xiaoheishu.auth.constant;

/**
 * @author DarkMatter
 * @version 1.0
 * @Date 2024-07-20 20:34
 * @description TODO
 */
public class RedisKeyConstants {

    /**
     * 验证码前缀
     */
    private static final String VERIFICATION_CODE_KEY_PREFIX = "verification_code:";

    /**
     * 小哈书全局生成器 key
     */
    public static final String XIAOHEISHU_ID_GENERATOR_KEY = "xiaoheishu.id.generator" ;

    /**
     * 用户角色数据 key 前缀
     */
    public static final String USER_ROLES_KEY_PREFIX = "user:roles:";

    /**
     * 角色对应的权限集合 key 前缀
     */
    public static final String ROLE_PERMISSIONS_KEY_PREFIX = "role:permissions:";

    /**
     * 构建验证码前缀
     * @param phone 手机号
     * @return
     */
    public static String buildVerificationCodeKey(String phone) {
        return VERIFICATION_CODE_KEY_PREFIX + phone;
    }

    /**
     * 构建验证码 key
     * @param phone 手机号
     * @return
     */
    public static String buildUserRoleKey(Long userId) {
        return USER_ROLES_KEY_PREFIX + userId;
    }

    /**
     * 构建角色对应的权限集合 key
     * @param roleId 角色 id
     * @return
     */
    public static String buildRolePermissionsKey(String roleKey) {
        return ROLE_PERMISSIONS_KEY_PREFIX + roleKey;
    }
}
