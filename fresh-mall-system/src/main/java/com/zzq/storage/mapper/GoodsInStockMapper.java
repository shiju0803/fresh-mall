package com.zzq.storage.mapper;

import com.zzq.common.core.mapper.BaseMapperPlus;
import com.zzq.storage.domain.GoodsInStock;
import com.zzq.storage.domain.vo.GoodsInStockVo;

/**
 * 商品入库Mapper接口
 */
public interface GoodsInStockMapper extends BaseMapperPlus<GoodsInStockMapper, GoodsInStock, GoodsInStockVo> {

    GoodsInStock selectByMax();
}
