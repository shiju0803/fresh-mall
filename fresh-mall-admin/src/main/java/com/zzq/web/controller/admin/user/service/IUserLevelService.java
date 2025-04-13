package com.zzq.web.controller.admin.user.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserLevelBo;
import com.zzq.user.domain.vo.UserLevelVo;

import java.util.Collection;
import java.util.List;

/**
 * 用户等级Service接口
 * @date 2023-02-14
 */
public interface IUserLevelService {

    /**
     * 查询用户等级
     */
    UserLevelVo queryById(Long id);

    /**
     * 查询用户等级列表
     */
    TableDataInfo<UserLevelVo> queryPageList(UserLevelBo bo, PageQuery pageQuery);

    /**
     * 查询用户等级列表
     */
    List<UserLevelVo> queryList(UserLevelBo bo);

    /**
     * 新增用户等级
     */
    Boolean insertByBo(UserLevelBo bo);

    /**
     * 修改用户等级
     */
    Boolean updateByBo(UserLevelBo bo);

    /**
     * 校验并批量删除用户等级信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
