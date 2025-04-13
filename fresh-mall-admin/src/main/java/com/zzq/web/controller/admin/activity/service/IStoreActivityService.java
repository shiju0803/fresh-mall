package com.zzq.web.controller.admin.activity.service;

import com.zzq.activity.domain.bo.StoreActivityBo;
import com.zzq.activity.domain.vo.StoreActivityVo;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 活动商品Service接口
 */
public interface IStoreActivityService {

    /**
     * 查询活动商品
     */
    StoreActivityVo queryById(Long id);

    /**
     * 查询活动商品列表
     */
    TableDataInfo<StoreActivityVo> queryPageList(StoreActivityBo bo, PageQuery pageQuery);

    /**
     * 查询活动商品列表
     */
    List<StoreActivityVo> queryList(StoreActivityBo bo);

    /**
     * 新增活动商品
     */
    Boolean insertByBo(StoreActivityBo bo);

    /**
     * 修改活动商品
     */
    Boolean updateByBo(StoreActivityBo bo);

    /**
     * 校验并批量删除活动商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

}
