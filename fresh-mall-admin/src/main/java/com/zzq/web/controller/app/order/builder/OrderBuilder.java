package com.zzq.web.controller.app.order.builder;


import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.bo.OrderPriceBo;
import com.zzq.order.domain.bo.OrderRequestBo;

/**
 * @description: 抽象建造者
 **/
public abstract class OrderBuilder {

    /**
     * 1.订单初始创建校验部分
     */
    public abstract void buildOrderCheckHandlePart(OrderRequestBo orderRequest, Long userId);

    /**
     * 2.订单价格处理部分
     */
    public abstract OrderPriceBo buildOrderPriceHandlePart(StoreOrder orderDO, OrderRequestBo orderRequest, Long userId);

    /**
     * 3.构建订单部分
     */
    public abstract void buildOrderHandlePart(StoreOrder orderDO, OrderPriceBo orderPriceDTO, OrderRequestBo orderRequest, String channel, Long userId);

    /**
     * 4.更新优惠券部分
     */
    public abstract void buildCoupontHandlePart(StoreOrder orderDO);

    /**
     * 5.订单商品SKU部分
     */
    public abstract void buildOrderSkuHandlePart(StoreOrder orderDO, OrderPriceBo orderPriceDTO, OrderRequestBo orderRequest);

    /**
     * 6.购物车部分
     */
    public abstract void buildCartHandlePart(OrderRequestBo orderRequest, Long userId);

    /**
     * 7.触发订单创建完成后通知事件部分
     */
    public abstract void buildNotifyHandlePart(StoreOrder orderDO);
}
