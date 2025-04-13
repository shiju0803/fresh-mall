package com.zzq.web.controller.admin.user.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserLevelSettingBo;
import com.zzq.user.domain.vo.UserLevelSettingVo;

import java.util.Collection;
import java.util.List;

/**
 * 设置用户等级Service接口
 * @date 2023-02-21
 */
public interface IUserLevelSettingService {

    /**
     * 查询设置用户等级
     */
    UserLevelSettingVo queryById(Long id);

    /**
     * 查询设置用户等级列表
     */
    TableDataInfo<UserLevelSettingVo> queryPageList(UserLevelSettingBo bo, PageQuery pageQuery);

    /**
     * 查询设置用户等级列表
     */
    List<UserLevelSettingVo> queryList(UserLevelSettingBo bo);

    /**
     * 新增设置用户等级
     */
    Boolean insertByBo(UserLevelSettingBo bo);

    /**
     * 修改设置用户等级
     */
    Boolean updateByBo(UserLevelSettingBo bo);

    /**
     * 校验并批量删除设置用户等级信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
