package com.zzq.web.controller.admin.storage.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.storage.domain.bo.GoodsOutStockBo;
import com.zzq.storage.domain.vo.GoodsOutStockVo;
import com.zzq.storage.domain.vo.StorageVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品出库Service接口
 */
public interface IGoodsOutStockService {

    /**
     * 查询商品出库
     */
    GoodsOutStockVo queryById(Long id);

    /**
     * 查询商品出库列表
     */
    TableDataInfo<GoodsOutStockVo> queryPageList(GoodsOutStockBo bo, PageQuery pageQuery);

    /**
     * 查询商品出库列表
     */
    List<GoodsOutStockVo> queryList(GoodsOutStockBo bo);

    /**
     * 新增商品出库
     */
    Boolean insertByBo(GoodsOutStockBo bo);

    /**
     * 修改商品出库
     */
    Boolean updateByBo(GoodsOutStockBo bo);

    /**
     * 校验并批量删除商品出库信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 出库
     * @param bo
     * @return
     */
    Boolean updateOutOfStock(GoodsOutStockBo bo);

    /**
     * 获取所有仓库的名称
     * @param bo
     * @return
     */
    List<StorageVo> storagAllName(GoodsOutStockBo bo);
}
