package com.zzq.web.controller.admin.storage.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.storage.domain.bo.InStockProductBo;
import com.zzq.storage.domain.vo.InStockProductVo;

import java.util.Collection;
import java.util.List;

/**
 * 入库商品Service接口
 */
public interface IInStockProductService {

    /**
     * 查询入库商品
     */
    InStockProductVo queryById(Long id);

    /**
     * 查询入库商品列表
     */
    TableDataInfo<InStockProductVo> queryPageList(InStockProductBo bo, PageQuery pageQuery);

    /**
     * 查询入库商品列表
     */
    List<InStockProductVo> queryList(InStockProductBo bo);

    /**
     * 新增入库商品
     */
    Boolean insertByBo(InStockProductBo bo);

    /**
     * 修改入库商品
     */
    Boolean updateByBo(InStockProductBo bo);

    /**
     * 校验并批量删除入库商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
