package com.zzq.storage.mapper;

import com.zzq.common.core.mapper.BaseMapperPlus;
import com.zzq.storage.domain.GoodsOutStock;
import com.zzq.storage.domain.vo.GoodsOutStockVo;

/**
 * 商品出库Mapper接口
 */
public interface GoodsOutStockMapper extends BaseMapperPlus<GoodsOutStockMapper, GoodsOutStock, GoodsOutStockVo> {


    /**
     * 出库商品数更新
     *
     * @return
     */
    GoodsOutStock selectByMax();
}
