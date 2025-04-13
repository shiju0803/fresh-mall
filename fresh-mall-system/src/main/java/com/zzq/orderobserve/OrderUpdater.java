package com.zzq.orderobserve;

import com.zzq.order.domain.StoreOrder;

/**
 * 观察者对象
 */
public class OrderUpdater {

    /**
     * 订单对象
     */
    private StoreOrder orderDo;


    /**
     * 默认构造参数
     *
     * @param orderDo 订单对象
     */
    public OrderUpdater(StoreOrder orderDo) {
        this.orderDo = orderDo;
    }

    public StoreOrder getOrderDo() {
        return orderDo;
    }

    public void setOrderDo(StoreOrder orderDo) {
        this.orderDo = orderDo;
    }
}
