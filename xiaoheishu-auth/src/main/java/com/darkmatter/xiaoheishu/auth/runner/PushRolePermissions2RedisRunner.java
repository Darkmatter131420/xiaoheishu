package com.darkmatter.xiaoheishu.auth.runner;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.darkmatter.framework.common.util.JsonUtils;
import com.darkmatter.xiaoheishu.auth.constant.RedisKeyConstants;
import com.darkmatter.xiaoheishu.auth.domain.dataobject.PermissionDO;
import com.darkmatter.xiaoheishu.auth.domain.dataobject.RoleDO;
import com.darkmatter.xiaoheishu.auth.domain.dataobject.RolePermissionDO;
import com.darkmatter.xiaoheishu.auth.domain.mapper.PermissionDOMapper;
import com.darkmatter.xiaoheishu.auth.domain.mapper.RoleDOMapper;
import com.darkmatter.xiaoheishu.auth.domain.mapper.RolePermissionDOMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author DarkMatter
 * @version v1.0.0
 * @date 2024/8/6 11:47
 * @description 推送角色权限数据到 Redis
 */
@Component
@Slf4j
public class PushRolePermissions2RedisRunner implements ApplicationRunner {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    @Resource
    private RolePermissionDOMapper rolePermissionDOMapper;

    @Resource
    private RoleDOMapper roleDOMapper;

    @Resource
    private PermissionDOMapper permissionDOMapper;

    // 权限同步标记 key
    private static final String PUSH_PERMISSION_FLAG = "push.permission.flag";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("==> 开始同步角色权限数据到 Redis 中...");

        // todo
        try {
            // 是否能够同步数据：原子操作，只有在键 PUSH_PERMISSION_FLAG 不存在时，才能设置该键的键值为一，并设置过期时间为一天
            Boolean canPushed = redisTemplate.opsForValue().setIfAbsent(PUSH_PERMISSION_FLAG, "1", 1, TimeUnit.DAYS);

            // 如果无法同步数据
            if (!canPushed) {
                log.info("==> 角色权限数据已经同步到 Redis 中，不再同步...");
                return;
            }

            // 查询出所有角色
            List<RoleDO> roleDOS = roleDOMapper.selectEnabledList();

            if (CollUtil.isNotEmpty(roleDOS)) {
                // 拿到所有角色的 id
                List<Long> roleIds = roleDOS.stream().map(RoleDO::getId).toList();

                // 根据角色 id 集合批量查询出所有角色对应权限
                List<RolePermissionDO> rolePermissionDOS = rolePermissionDOMapper.selectByRoleIds(roleIds);
                // 按角色 id 分组，每个角色id对应多个权限id
                Map<Long, List<Long>> roleIdPermissionIdsMap = rolePermissionDOS.stream().collect(
                        Collectors.groupingBy(RolePermissionDO::getRoleId,
                                Collectors.mapping(RolePermissionDO::getPermissionId, Collectors.toList()))
                );

                // 查询 App 端所有被启用的权限
                List<PermissionDO> permissionDOS = permissionDOMapper.selectAppEnabledList();
                // 权限 ID - 权限DO
                Map<Long, PermissionDO> permissionIdDOMap = permissionDOS.stream().collect(
                        Collectors.toMap(PermissionDO::getId, permissionDO -> permissionDO)
                );

                // 组织角色 - 权限关系
                Map<String, List<String>> roleKeyPermissionsMap = Maps.newHashMap();

                // 循环所有角色
                roleDOS.forEach(roleDO -> {
                    // 当前角色 id
                    Long roleId = roleDO.getId();
                    // 当前角色 key
                    String roleKey = roleDO.getRoleKey();
                    // 当前角色 ID 对应的权限 id 集合
                    List<Long> permissionIds = roleIdPermissionIdsMap.get(roleId);
                    if (CollUtil.isNotEmpty(permissionIds)) {
                        // 当前角色对应的权限集合
                        List<String> permissionKeys = Lists.newArrayList();
                        permissionIds.forEach(permissionId -> {
                            // 根据权限 id 获取对应的权限 DO 对象
                            PermissionDO permissionDO = permissionIdDOMap.get(permissionId);
                            permissionKeys.add(permissionDO.getPermissionKey());
                        });
                        roleKeyPermissionsMap.put(roleKey, permissionKeys);
                    }
                });

                // 同步至 Redis 中， 方便后续网关查询鉴权使用
                roleKeyPermissionsMap.forEach((roleKey, permissions) -> {
                    String key = RedisKeyConstants.buildRolePermissionsKey(roleKey);
                    redisTemplate.opsForValue().set(key, JsonUtils.toJsonString(permissions));
                });
            }

            log.info("==> 成功同步角色权限数据到 Redis 中...");
        } catch (Exception e) {
            log.error("==> 同步角色权限数据到 Redis 中异常：", e);
        }
    }
}
