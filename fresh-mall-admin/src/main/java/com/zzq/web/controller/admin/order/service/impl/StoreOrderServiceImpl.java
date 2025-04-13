package com.zzq.web.controller.admin.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.OrderStatusType;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.OrderUtil;
import com.zzq.common.utils.StringUtils;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.StoreOrderProduct;
import com.zzq.order.domain.bo.StoreOrderBo;
import com.zzq.order.domain.vo.StoreOrderVo;
import com.zzq.order.mapper.StoreOrderMapper;
import com.zzq.order.mapper.StoreOrderProductMapper;
import com.zzq.user.domain.vo.UserVo;
import com.zzq.web.controller.admin.order.service.IStoreOrderService;
import com.zzq.web.controller.admin.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 订单Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreOrderServiceImpl implements IStoreOrderService {

    private static final Logger logger = LoggerFactory.getLogger(StoreOrderServiceImpl.class);

    private final StoreOrderMapper baseMapper;

    private final StoreOrderProductMapper orderProductMapper;

    private final IUserService kxUserService;

    /**
     * 查询订单
     */
    @Override
    public StoreOrderVo queryById(Long id) {
        StoreOrderVo kxStoreOrderVo = baseMapper.selectVoById(id);
        if (ObjectUtil.isEmpty(kxStoreOrderVo)) {
            throw new ServiceException("订单详情不存在");
        }

        String payTypeName = OrderUtil.payTypeName("weixin"
                , 1);
        kxStoreOrderVo.setPayTypeName(payTypeName);

        //新增记录产品详情
        kxStoreOrderVo.setProductList(orderProductMapper.selectVoList(new LambdaQueryWrapper<StoreOrderProduct>().eq(StoreOrderProduct::getOrderId, kxStoreOrderVo.getId())));

        //添加用户信息
        kxStoreOrderVo.setUserVo(kxUserService.selectByUid(kxStoreOrderVo.getUid()));
        if (kxStoreOrderVo.getUserVo() == null) {
            kxStoreOrderVo.setUserVo(new UserVo());
        }
        return kxStoreOrderVo;
    }

    /**
     * 查询订单列表
     */
    @Override
    public TableDataInfo<StoreOrderVo> queryPageList(StoreOrderBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreOrder> lqw = buildQueryWrapper(bo);

        List<Integer> status = new ArrayList<>();
        if (StringUtils.isNoneBlank(bo.getOrderStatus())) {
            if (!"all".equals(bo.getOrderStatus())) {
                String[] states = bo.getOrderStatus().split(",");
                for (int i = 0; i < states.length; i++) {
                    status.add(Integer.parseInt(states[i]));
                }
                lqw.in(StoreOrder::getStatus, status);
            }
        }
        if (ObjectUtils.isNotEmpty(bo.getStartTime()) && ObjectUtils.isNotEmpty(bo.getEndTime())) {
            lqw.between(StoreOrder::getCreateTime, bo.getStartTime(), bo.getEndTime());
        }
        if (ObjectUtils.isNotEmpty(bo.getStorageId())) {
            lqw.eq(StoreOrder::getStoreId, bo.getStorageId());
        }
        Page<StoreOrderVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询订单列表
     */
    @Override
    public List<StoreOrderVo> queryList(StoreOrderBo bo) {
        LambdaQueryWrapper<StoreOrder> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreOrder> buildQueryWrapper(StoreOrderBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreOrder> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getOrderId()), StoreOrder::getOrderId, bo.getOrderId());
        lqw.eq(bo.getUid() != null, StoreOrder::getUid, bo.getUid());
        lqw.like(StringUtils.isNotBlank(bo.getRealName()), StoreOrder::getRealName, bo.getRealName());
        lqw.likeRight(StringUtils.isNotBlank(bo.getUserPhone()), StoreOrder::getUserPhone, bo.getUserPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getUserAddress()), StoreOrder::getUserAddress, bo.getUserAddress());
        lqw.eq(StringUtils.isNotBlank(bo.getCartId()), StoreOrder::getCartId, bo.getCartId());
        lqw.eq(bo.getFreightPrice() != null, StoreOrder::getFreightPrice, bo.getFreightPrice());
        lqw.eq(bo.getTotalNum() != null, StoreOrder::getTotalNum, bo.getTotalNum());
        lqw.eq(bo.getTotalPrice() != null, StoreOrder::getTotalPrice, bo.getTotalPrice());
        lqw.eq(bo.getTotalPostage() != null, StoreOrder::getTotalPostage, bo.getTotalPostage());
        lqw.eq(bo.getPayPrice() != null, StoreOrder::getPayPrice, bo.getPayPrice());
        lqw.eq(bo.getPayPostage() != null, StoreOrder::getPayPostage, bo.getPayPostage());
        lqw.eq(bo.getDeductionPrice() != null, StoreOrder::getDeductionPrice, bo.getDeductionPrice());
        lqw.eq(bo.getCouponId() != null, StoreOrder::getCouponId, bo.getCouponId());
        lqw.eq(bo.getCouponPrice() != null, StoreOrder::getCouponPrice, bo.getCouponPrice());
        lqw.eq(bo.getPayTime() != null, StoreOrder::getPayTime, bo.getPayTime());
        lqw.eq(bo.getStatus() != null, StoreOrder::getStatus, bo.getStatus());
        lqw.eq(bo.getRefundStatus() != null, StoreOrder::getRefundStatus, bo.getRefundStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundReasonWapImg()), StoreOrder::getRefundReasonWapImg, bo.getRefundReasonWapImg());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundReasonWapExplain()), StoreOrder::getRefundReasonWapExplain, bo.getRefundReasonWapExplain());
        lqw.eq(bo.getRefundReasonTime() != null, StoreOrder::getRefundReasonTime, bo.getRefundReasonTime());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundReasonWap()), StoreOrder::getRefundReasonWap, bo.getRefundReasonWap());
        lqw.eq(StringUtils.isNotBlank(bo.getRefundReason()), StoreOrder::getRefundReason, bo.getRefundReason());
        lqw.eq(bo.getRefundPrice() != null, StoreOrder::getRefundPrice, bo.getRefundPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliverySn()), StoreOrder::getDeliverySn, bo.getDeliverySn());
        lqw.like(StringUtils.isNotBlank(bo.getDeliveryName()), StoreOrder::getDeliveryName, bo.getDeliveryName());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryType()), StoreOrder::getDeliveryType, bo.getDeliveryType());
        lqw.eq(StringUtils.isNotBlank(bo.getDeliveryId()), StoreOrder::getDeliveryId, bo.getDeliveryId());
        lqw.eq(bo.getGainIntegral() != null, StoreOrder::getGainIntegral, bo.getGainIntegral());
        lqw.eq(bo.getUseIntegral() != null, StoreOrder::getUseIntegral, bo.getUseIntegral());
        lqw.eq(bo.getPayIntegral() != null, StoreOrder::getPayIntegral, bo.getPayIntegral());
        lqw.eq(bo.getBackIntegral() != null, StoreOrder::getBackIntegral, bo.getBackIntegral());
        lqw.eq(StringUtils.isNotBlank(bo.getMark()), StoreOrder::getMark, bo.getMark());
        lqw.eq(bo.getIsDel() != null, StoreOrder::getIsDel, bo.getIsDel());
        lqw.eq(bo.getMerId() != null, StoreOrder::getMerId, bo.getMerId());
        lqw.eq(bo.getIsMerCheck() != null, StoreOrder::getIsMerCheck, bo.getIsMerCheck());
        lqw.eq(bo.getCombinationId() != null, StoreOrder::getCombinationId, bo.getCombinationId());
        lqw.eq(bo.getPinkId() != null, StoreOrder::getPinkId, bo.getPinkId());
        lqw.eq(bo.getCost() != null, StoreOrder::getCost, bo.getCost());
        lqw.eq(bo.getSeckillId() != null, StoreOrder::getSeckillId, bo.getSeckillId());
        lqw.eq(bo.getBargainId() != null, StoreOrder::getBargainId, bo.getBargainId());
        lqw.eq(StringUtils.isNotBlank(bo.getVerifyCode()), StoreOrder::getVerifyCode, bo.getVerifyCode());
        lqw.eq(bo.getStoreId() != null, StoreOrder::getStoreId, bo.getStoreId());
        lqw.eq(bo.getShippingType() != null, StoreOrder::getShippingType, bo.getShippingType());
        lqw.eq(bo.getIsChannel() != null, StoreOrder::getIsChannel, bo.getIsChannel());
        lqw.eq(bo.getIsRemind() != null, StoreOrder::getIsRemind, bo.getIsRemind());
        lqw.eq(bo.getIsSystemDel() != null, StoreOrder::getIsSystemDel, bo.getIsSystemDel());


        return lqw;
    }


    /**
     * 新增订单
     */
    @Override
    public Boolean insertByBo(StoreOrderBo bo) {
        StoreOrder add = BeanUtil.toBean(bo, StoreOrder.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改订单
     */
    @Override
    public Boolean updateByBo(StoreOrderBo bo) {
        StoreOrderVo kxStoreOrderVo = baseMapper.selectVoById(bo.getId());
        if (ObjectUtil.isNull(kxStoreOrderVo)) {
            throw new ServiceException("订单不存在");
        }
        StoreOrder update = BeanUtil.toBean(bo, StoreOrder.class);
        baseMapper.updateById(update);
        return true;

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreOrder entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除订单
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    @Override
    public Boolean startStocking(Long id) {
        return this.updateOrderStatus(id, OrderStatusType.PREPARING_GOODS.getCode(), OrderStatusType.WAIT_PREPARE_GOODS.getCode());
    }

    @Override
    public Boolean completeAllocation(Long id) {
        return this.updateOrderStatus(id, OrderStatusType.WAIT_STOCK.getCode(), OrderStatusType.PREPARING_GOODS.getCode());
    }

    @Override
    public Boolean merchantDistribution(Long id) {
        return this.updateOrderStatus(id, OrderStatusType.WAIT_CONFIRM.getCode(), OrderStatusType.WAIT_STOCK.getCode());
    }

    @Override
    public Boolean completeDelivery(Long id) {
        return this.updateOrderStatus(id, OrderStatusType.WAIT_APPRAISE.getCode(), OrderStatusType.WAIT_CONFIRM.getCode());
    }

    /**
     * 更新状态
     *
     * @param id
     * @param newStatus
     * @param oldStatus
     * @return
     */
    private Boolean updateOrderStatus(Long id, Integer newStatus, Integer oldStatus) {
        try {
            StoreOrder storeOrder = StoreOrder.builder().build();
            storeOrder.setStatus(newStatus);
            return baseMapper.update(storeOrder, new QueryWrapper<StoreOrder>().eq("id" , id).eq("status" , oldStatus)) > 0;
        } catch (Exception e) {
            logger.error("[订单状态扭转] 异常" , e);
            throw new ServiceException("订单系统未知异常");
        }
    }


}
