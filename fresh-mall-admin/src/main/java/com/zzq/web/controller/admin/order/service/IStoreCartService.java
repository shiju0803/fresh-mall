package com.zzq.web.controller.admin.order.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.order.domain.bo.StoreCartBo;
import com.zzq.order.domain.vo.StoreCartVo;

import java.util.Collection;
import java.util.List;

/**
 * 购物车Service接口
 */
public interface IStoreCartService {

    /**
     * 查询购物车
     */
    StoreCartVo queryById(Long id);

    /**
     * 查询购物车列表
     */
    TableDataInfo<StoreCartVo> queryPageList(StoreCartBo bo, PageQuery pageQuery);

    /**
     * 查询购物车列表
     */
    List<StoreCartVo> queryList(StoreCartBo bo);

    /**
     * 新增购物车
     */
    Boolean insertByBo(StoreCartBo bo);

    /**
     * 修改购物车
     */
    Boolean updateByBo(StoreCartBo bo);

    /**
     * 校验并批量删除购物车信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
