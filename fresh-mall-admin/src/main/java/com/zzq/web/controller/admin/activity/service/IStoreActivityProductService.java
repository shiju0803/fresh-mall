package com.zzq.web.controller.admin.activity.service;

import com.zzq.activity.domain.bo.StoreActivityProductBo;
import com.zzq.activity.domain.vo.StoreActivityProductVo;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 活动商品Service接口
 */
public interface IStoreActivityProductService {

    /**
     * 查询活动商品
     */
    StoreActivityProductVo queryById(Long id);

    /**
     * 查询活动商品列表
     */
    TableDataInfo<StoreActivityProductVo> queryPageList(StoreActivityProductBo bo, PageQuery pageQuery);

    /**
     * 查询活动商品列表
     */
    List<StoreActivityProductVo> queryList(StoreActivityProductBo bo);

    /**
     * 新增活动商品
     */
    Boolean insertByBo(StoreActivityProductBo bo);

    /**
     * 修改活动商品
     */
    Boolean updateByBo(StoreActivityProductBo bo);

    /**
     * 校验并批量删除活动商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Boolean addProductBatch(StoreActivityProductBo bo);

    Boolean updatePrice(StoreActivityProductBo bo);
}
