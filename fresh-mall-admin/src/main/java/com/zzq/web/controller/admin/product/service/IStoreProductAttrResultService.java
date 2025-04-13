package com.zzq.web.controller.admin.product.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.product.domain.bo.StoreProductAttrResultBo;
import com.zzq.product.domain.vo.StoreProductAttrResultVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性详情Service接口
 */
public interface IStoreProductAttrResultService {

    /**
     * 查询商品属性详情
     */
    StoreProductAttrResultVo queryById(Long id);

    /**
     * 查询商品属性详情列表
     */
    TableDataInfo<StoreProductAttrResultVo> queryPageList(StoreProductAttrResultBo bo, PageQuery pageQuery);

    /**
     * 查询商品属性详情列表
     */
    List<StoreProductAttrResultVo> queryList(StoreProductAttrResultBo bo);

    /**
     * 新增商品属性详情
     */
    Boolean insertByBo(StoreProductAttrResultBo bo);

    /**
     * 修改商品属性详情
     */
    Boolean updateByBo(StoreProductAttrResultBo bo);

    /**
     * 校验并批量删除商品属性详情信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
