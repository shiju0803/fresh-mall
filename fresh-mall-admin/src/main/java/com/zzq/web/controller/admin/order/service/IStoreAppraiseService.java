package com.zzq.web.controller.admin.order.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.order.domain.bo.StoreAppraiseBo;
import com.zzq.order.domain.vo.StoreAppraiseVo;

import java.util.Collection;
import java.util.List;

/**
 * 评论管理Service接口
 */
public interface IStoreAppraiseService {

    /**
     * 查询评论管理
     */
    StoreAppraiseVo queryById(Long id);

    /**
     * 查询评论管理列表
     */
    TableDataInfo<StoreAppraiseVo> queryPageList(StoreAppraiseBo bo, PageQuery pageQuery);

    /**
     * 查询评论管理列表
     */
    List<StoreAppraiseVo> queryList(StoreAppraiseBo bo);

    /**
     * 新增评论管理
     */
    Boolean insertByBo(StoreAppraiseBo bo);

    /**
     * 修改评论管理
     */
    Boolean updateByBo(StoreAppraiseBo bo);

    /**
     * 校验并批量删除评论管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 通过评论
     * @param id
     * @return
     */
    int changeState(Long id);
}
