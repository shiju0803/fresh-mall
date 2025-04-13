package com.zzq.web.controller.app.order.service;

import com.zzq.order.domain.vo.StoreCartVo;

import java.util.List;

public interface IAppCartService {

    /**
     * 获取购物车数量
     */
    Long countCart(Long storageId, Long userId);

    /**
     * 获取购物车商品列表
     */
    List<StoreCartVo> getCartList(Long storageId, Long userId);

    /**
     * 获取购物车商品列表
     * @param productId
     * @param num
     * @param userId
     * @return
     */
    StoreCartVo addCartItem(Long productId, Long num, Long userId);

    /**
     * 更新购物车数量
     */
    Long updateCartItemNum(Long cartId, Long num, Long userId);
    /**
     * 将购物车商品删除
     */
    Boolean removeCartItem(Long cartId, Long userId);
}
