package com.zzq.web.controller.app.order.builder;

import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.bo.OrderPriceBo;
import com.zzq.order.domain.bo.OrderRequestBo;

/**
 * @description: 指挥者
 **/
public class OrderDirector {

    private OrderBuilder builder;

    public OrderDirector(OrderBuilder builder) {
        this.builder = builder;
    }

    //构建与组装方法
    public void constructOrder(StoreOrder orderDO, final OrderRequestBo orderRequest, String channel, Long userId) {
        builder.buildOrderCheckHandlePart(orderRequest, userId);
        OrderPriceBo orderPriceBo = builder.buildOrderPriceHandlePart(orderDO, orderRequest, userId);
        builder.buildOrderHandlePart(orderDO, orderPriceBo, orderRequest, channel, userId);
        builder.buildCoupontHandlePart(orderDO);
        builder.buildOrderSkuHandlePart(orderDO, orderPriceBo, orderRequest);
        builder.buildCartHandlePart(orderRequest, userId);
        builder.buildNotifyHandlePart(orderDO);
    }

}
