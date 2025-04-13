package com.zzq.system.mapper;

import com.zzq.system.domain.SysUserRole;
import com.zzq.common.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 用户与角色关联表 数据层
 */
public interface SysUserRoleMapper extends BaseMapperPlus<SysUserRoleMapper, SysUserRole, SysUserRole> {

    List<Long> selectUserIdsByRoleId(Long roleId);

}
