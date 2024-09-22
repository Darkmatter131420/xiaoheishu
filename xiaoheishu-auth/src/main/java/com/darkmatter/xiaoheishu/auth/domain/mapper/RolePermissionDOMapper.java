package com.darkmatter.xiaoheishu.auth.domain.mapper;

import com.darkmatter.xiaoheishu.auth.domain.dataobject.RolePermissionDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RolePermissionDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(RolePermissionDO record);

    int insertSelective(RolePermissionDO record);

    RolePermissionDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(RolePermissionDO record);

    int updateByPrimaryKey(RolePermissionDO record);

    /**
     * 根据角色 id 集合批量查询
     *
     * @param roleIds
     * @return
     */
    List<RolePermissionDO> selectByRoleIds(@Param("roleIds") List<Long> roleIds);
}