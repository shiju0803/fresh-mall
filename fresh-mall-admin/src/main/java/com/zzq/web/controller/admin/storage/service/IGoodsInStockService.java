package com.zzq.web.controller.admin.storage.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.storage.domain.bo.GoodsInStockBo;
import com.zzq.storage.domain.vo.GoodsInStockVo;
import com.zzq.storage.domain.vo.StorageVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品入库Service接口
 */
public interface IGoodsInStockService {

    /**
     * 查询商品入库
     */
    GoodsInStockVo queryById(Long id);

    /**
     * 查询商品入库列表
     */
    TableDataInfo<GoodsInStockVo> queryPageList(GoodsInStockBo bo, PageQuery pageQuery);

    /**
     * 查询商品入库列表
     */
    List<GoodsInStockVo> queryList(GoodsInStockBo bo);

    /**
     * 新增商品入库
     */
    Boolean insertByBo(GoodsInStockBo bo);

    /**
     * 修改商品入库
     */
    Boolean updateByBo(GoodsInStockBo bo);

    /**
     * 校验并批量删除商品入库信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 入库
     * @param bo
     * @return
     */
    Boolean updateInOfStock(GoodsInStockBo bo);

    /**
     * 获取所有仓库的名称
     * @param bo
     * @return
     */
    List<StorageVo> storagAllName(GoodsInStockBo bo);
}
