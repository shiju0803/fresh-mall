package com.zzq.web.controller.admin.product.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.product.domain.bo.StoreProductAttrValueBo;
import com.zzq.product.domain.vo.StoreProductAttrValueVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性值Service接口
 */
public interface IStoreProductAttrValueService {

    /**
     * 查询商品属性值
     */
    StoreProductAttrValueVo queryById(Long id);

    /**
     * 查询商品属性值列表
     */
    TableDataInfo<StoreProductAttrValueVo> queryPageList(StoreProductAttrValueBo bo, PageQuery pageQuery);

    /**
     * 查询商品属性值列表
     */
    List<StoreProductAttrValueVo> queryList(StoreProductAttrValueBo bo);

    /**
     * 新增商品属性值
     */
    Boolean insertByBo(StoreProductAttrValueBo bo);

    /**
     * 修改商品属性值
     */
    Boolean updateByBo(StoreProductAttrValueBo bo);

    /**
     * 校验并批量删除商品属性值信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
