package com.zzq.web.controller.admin.storage.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.storage.domain.bo.OutStockProductBo;
import com.zzq.storage.domain.vo.OutStockProductVo;

import java.util.Collection;
import java.util.List;

/**
 * 出库商品Service接口
 */
public interface IOutStockProductService {

    /**
     * 查询出库商品
     */
    OutStockProductVo queryById(Long id);

    /**
     * 查询出库商品列表
     */
    TableDataInfo<OutStockProductVo> queryPageList(OutStockProductBo bo, PageQuery pageQuery);

    /**
     * 查询出库商品列表
     */
    List<OutStockProductVo> queryList(OutStockProductBo bo);

    /**
     * 新增出库商品
     */
    Boolean insertByBo(OutStockProductBo bo);

    /**
     * 修改出库商品
     */
    Boolean updateByBo(OutStockProductBo bo);

    /**
     * 校验并批量删除出库商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
