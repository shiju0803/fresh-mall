package com.zzq.web.controller.admin.order.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.order.domain.bo.StoreOrderBo;
import com.zzq.order.domain.vo.StoreOrderVo;

import java.util.Collection;
import java.util.List;

/**
 * 订单Service接口
 */
public interface IStoreOrderService {

    /**
     * 查询订单
     */
    StoreOrderVo queryById(Long id);

    /**
     * 查询订单列表
     */
    TableDataInfo<StoreOrderVo> queryPageList(StoreOrderBo bo, PageQuery pageQuery);

    /**
     * 查询订单列表
     */
    List<StoreOrderVo> queryList(StoreOrderBo bo);

    /**
     * 新增订单
     */
    Boolean insertByBo(StoreOrderBo bo);

    /**
     * 修改订单
     */
    Boolean updateByBo(StoreOrderBo bo);

    /**
     * 校验并批量删除订单信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 开始配货
     *
     * @param orderId
     * @return
     */
    Boolean startStocking(Long id);

    /**
     * 完成配货
     *
     * @param id
     * @return
     */
    Boolean completeAllocation(Long id);

    /**
     * 商家自配
     *
     * @param id
     * @return
     */
    Boolean merchantDistribution(Long id);

    /**
     * 完成配送
     *
     * @param id
     * @return
     */
    Boolean completeDelivery(Long id);
}
