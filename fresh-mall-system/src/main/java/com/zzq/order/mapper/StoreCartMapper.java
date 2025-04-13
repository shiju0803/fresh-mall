package com.zzq.order.mapper;

import com.zzq.order.domain.StoreCart;
import com.zzq.order.domain.vo.StoreCartVo;
import com.zzq.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车Mapper接口
 */
public interface StoreCartMapper extends BaseMapperPlus<StoreCartMapper, StoreCart, StoreCartVo> {

    /**
     * 获取购物车数量
     */
    Long countCart(@Param("userId") Long userId, @Param("storageId") Long storageId);

    /**
     * 获取购物车商品列表
     */
    List<StoreCartVo> getCartList(@Param("userId") Long userId, @Param("storageId") Long storageId);
}
