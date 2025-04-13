package com.zzq.web.controller.app.user.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserFootprintBo;
import com.zzq.user.domain.vo.UserFootprintVo;

import java.util.Collection;
import java.util.List;

/**
 * 足迹Service接口
 * @date 2023-04-06
 */
public interface IUserFootprintService {

    /**
     * 查询足迹
     */
    UserFootprintVo queryById(Long id);

    /**
     * 查询足迹列表
     */
    TableDataInfo<UserFootprintVo> queryPageList(UserFootprintBo bo, PageQuery pageQuery);

    /**
     * 查询足迹列表
     */
    List<UserFootprintVo> queryList(UserFootprintBo bo);

    /**
     * 新增足迹
     */
    Boolean insertByBo(UserFootprintBo bo);

    /**
     * 修改足迹
     */
    Boolean updateByBo(UserFootprintBo bo);

    /**
     * 校验并批量删除足迹信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 处理足迹
     *
     * @param userId
     * @param producId
     */
    boolean addOrUpdateFootprint(Long userId, Long producId);
}
