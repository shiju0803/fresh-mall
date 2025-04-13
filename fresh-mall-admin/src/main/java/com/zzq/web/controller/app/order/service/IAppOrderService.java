package com.zzq.web.controller.app.order.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.vo.StoreOrderVo;

import java.util.List;

public interface IAppOrderService {


    /**
     * 微信预付订单
     * @param orderId
     * @param userId
     * @param loginType
     * @param openId
     * @return
     */
    Object wxPrepay(String orderId, Long userId, Integer loginType, String openId);

    /**
     * 查询订单
     * @param wrapper
     * @return
     */
    List<StoreOrder> selectListByWrapper(QueryWrapper<StoreOrder> wrapper);

    /**
     * 更新状态
     * @param orderId
     * @param nowStatus
     * @param updateOrderDO
     */
    Boolean changeOrderStatus(String orderId, Integer nowStatus, StoreOrder updateOrderDO);

    /**
     *
     * @param pageNo
     * @param pageSize
     * @param state
     * @param userId
     * @return
     */
    TableDataInfo<StoreOrderVo> getOrderPage(Integer pageNo, Integer pageSize, String state, Long userId);

    /**
     * 取消订单
     * @param orderId
     * @param userId
     * @return
     */
    String cancel(String orderId, Long userId);

    /**
     * 确认订单
     * @param orderId
     * @param userId
     * @return
     */
    String confirm(String orderId, Long userId);

    /**
     * 获取订单详情
     * @param orderId
     * @param userId
     * @return
     */
    StoreOrderVo getOrderDetail(Long orderId, Long userId);


    List<StoreOrderVo> selectListVoByWrapper(QueryWrapper<StoreOrder> orderId);
}
