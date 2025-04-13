package com.zzq.web.controller.admin.product.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.product.domain.StoreProductAttr;
import com.zzq.product.domain.bo.StoreProductAttrBo;
import com.zzq.product.domain.vo.FromatDetailVo;
import com.zzq.product.domain.vo.ProductFormatVo;
import com.zzq.product.domain.vo.StoreProductAttrVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品属性Service接口
 */
public interface IStoreProductAttrService {

    /**
     * 查询商品属性
     */
    StoreProductAttrVo queryById(Long id);

    /**
     * 查询商品属性列表
     */
    TableDataInfo<StoreProductAttrVo> queryPageList(StoreProductAttrBo bo, PageQuery pageQuery);

    /**
     * 查询商品属性列表
     */
    List<StoreProductAttrVo> queryList(StoreProductAttrBo bo);

    /**
     * 新增商品属性
     */
    Boolean insertByBo(StoreProductAttrBo bo);

    /**
     * 修改商品属性
     */
    Boolean updateByBo(StoreProductAttrBo bo);

    /**
     * 校验并批量删除商品属性信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    void insertYxStoreProductAttr(List<FromatDetailVo> toList, List<ProductFormatVo> toList1, Long id);

    List<StoreProductAttr> queryListAll();
}
