package com.darkmatter.xiaoheishu.gateway.constant;

/**
 * @Author DarkMatter
 * @Date 2024年9月10日 17点30分
 * @Version v1.0.0
 * @Description TODO
 */
public class RedisKeyConstants {
    public static final String USER_ROLES_KEY_PREFIX = "user:roles:";

    /**
     * 角色对应的权限集合 key 前缀
     */
    public static final String ROLE_PERMISSIONS_KEY_PREFIX = "role:permissions:";

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
