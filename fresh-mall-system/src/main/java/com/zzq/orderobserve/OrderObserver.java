package com.zzq.orderobserve;

/**
 * 监听器
 */
public interface OrderObserver {

    /**
     * 处理
     *
     * @param obj 默认传参对象
     */
    void doSomeThing(OrderUpdater obj);

}
