package com.zzq.web.controller.app.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.OrderStatusType;
import com.zzq.common.enums.PayMethodEnum;
import com.zzq.common.enums.UserLoginType;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.IpUtil;
import com.zzq.common.utils.StringUtils;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.StoreOrderProduct;
import com.zzq.order.domain.vo.StoreOrderProductVo;
import com.zzq.order.domain.vo.StoreOrderVo;
import com.zzq.order.mapper.StoreOrderMapper;
import com.zzq.order.mapper.StoreOrderProductMapper;
import com.zzq.web.controller.app.order.service.IAppOrderService;
import com.zzq.wechat.WxPayConfiguration;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AppOrderService implements IAppOrderService {

    private static final Logger logger = LoggerFactory.getLogger(AppOrderService.class);

    private final StoreOrderMapper baseMapper;

    private final StoreOrderProductMapper orderProductMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Object wxPrepay(String orderId, Long userId, Integer loginType, String openId) {
        Date now = new Date();
        StoreOrder updateOrderDO = this.checkOrderExist(orderId, userId);
        // 检测订单状态
        Integer status = updateOrderDO.getStatus();
        if (status != OrderStatusType.UNPAY.getCode()) {
            throw new ServiceException("订单状态不支持支付");
        }

        String tradeType;
        WxPayService wxPayService;
        if (UserLoginType.MP_WEIXIN.getCode() == loginType) {
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.MINI);
            tradeType = WxPayConstants.TradeType.JSAPI;
        } else if (UserLoginType.APP_WEIXIN.getCode() == loginType || UserLoginType.REGISTER.getCode() == loginType) {
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.APP);
            tradeType = WxPayConstants.TradeType.APP;
        } else if (UserLoginType.H5_WEIXIN.getCode() == loginType) {
            wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.H5);
            tradeType = WxPayConstants.TradeType.JSAPI;
        } else {
            throw new ServiceException("当前平台不支持微信支付");
        }

        Object result;
        try {
            WxPayUnifiedOrderRequest orderRequest = new WxPayUnifiedOrderRequest();
            orderRequest.setOutTradeNo(orderId);
            orderRequest.setOpenid(openId);
            String nonceStr = "ibuaiVcKdpRxkhJA";
            String body = "订单说明" + orderId;
            orderRequest.setBody(body);
            orderRequest.setAppid(wxPayService.getConfig().getAppId());
            orderRequest.setTotalFee(updateOrderDO.getPayPrice().intValue());
            orderRequest.setSpbillCreateIp(IpUtil.getLocalIP());
            orderRequest.setTradeType(tradeType);
            orderRequest.setNonceStr(nonceStr);
            orderRequest.setNotifyUrl(wxPayService.getConfig().getNotifyUrl());
            result = wxPayService.createOrder(orderRequest);
        } catch (WxPayException e) {
            logger.error("[微信支付] 异常", e);
            throw new ServiceException(e.getErrCodeDes());
        } catch (Exception e) {
            logger.error("[预付款异常]", e);
            throw new ServiceException("订单系统未知异常");
        }
        return result;
    }

    @Override
    public List<StoreOrder> selectListByWrapper(QueryWrapper<StoreOrder> wrapper) {
        return baseMapper.selectList(wrapper);
    }

    @Override
    public Boolean changeOrderStatus(String orderId, Integer nowStatus, StoreOrder updateOrderDO) {
        try {
            // 防止传入值为空,导致其余订单被改变
            if (orderId == null || updateOrderDO == null) {
                throw new ServiceException("订单状态流转失败！");
            }

            if (baseMapper.update(updateOrderDO,
                new QueryWrapper<StoreOrder>()
                    .eq("order_id", orderId)
                    .eq("status", nowStatus)) > 0) {
                return true;
            }
            throw new ServiceException("订单状态流转失败！");

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[订单状态流转] 异常", e);
            throw new ServiceException("订单系统未知异常");
        }
    }

    @Override
    public TableDataInfo<StoreOrderVo> getOrderPage(Integer pageNo, Integer pageSize, String state, Long userId) {
        List<Integer> status = new ArrayList<>();
        if (StringUtils.isNoneBlank(state)) {
            String[] states = state.split(",");
            for (int i = 0; i < states.length; i++) {
                status.add(Integer.parseInt(states[i]));
            }
        }
        List<StoreOrderVo> orderVos = baseMapper.selectOrderPages(status, (pageNo - 1) * pageSize, pageSize, userId);
        Long count = baseMapper.countOrders(status, (pageNo - 1) * pageSize, pageSize, userId);
        //封装SKU
        orderVos.forEach(item -> {
            item.setProductList(orderProductMapper.selectVoList(new QueryWrapper<StoreOrderProduct>().eq("order_id", item.getId())));
        });
        return new TableDataInfo<StoreOrderVo>(orderVos, count);
    }

    private StoreOrder checkOrderExist(String orderId, Long userId) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<StoreOrder>().eq("order_id", orderId);
        if (userId != null) {
            wrapper.eq("uid", userId);
        }
        List<StoreOrder> storeOrderList = baseMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(storeOrderList)) {
            throw new ServiceException("订单不存在");
        }
        return storeOrderList.get(0);
    }


    @Override
    public String cancel(String orderId, Long userId) {
        StoreOrder storeOrder = this.checkOrderExist(orderId, userId);
        if (storeOrder.getStatus() != OrderStatusType.UNPAY.getCode()) {
            throw new ServiceException("订单状态不支持取消");
        }
        StoreOrder updateOrderDO = StoreOrder.builder().build();
        updateOrderDO.setStatus(OrderStatusType.CANCELED.getCode());
        updateOrderDO.setUpdateTime(new Date());
        this.changeOrderStatus(orderId, OrderStatusType.UNPAY.getCode(), updateOrderDO);
        return "ok";
    }

    @Override
    public String confirm(String orderId, Long userId) {
        StoreOrder orderDO = this.checkOrderExist(orderId, userId);
        if (orderDO.getStatus() != OrderStatusType.WAIT_CONFIRM.getCode()) {
            throw new ServiceException("订单状态不支持确认收货");
        }
        StoreOrder updateOrderDO = StoreOrder.builder().build();
        updateOrderDO.setStatus(OrderStatusType.WAIT_APPRAISE.getCode());
        updateOrderDO.setUpdateTime(new Date());
        this.changeOrderStatus(orderId, OrderStatusType.WAIT_CONFIRM.getCode(), updateOrderDO);
        return "ok";
    }

    @Override
    public StoreOrderVo getOrderDetail(Long orderId, Long userId) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<StoreOrder>()
            .eq("id", orderId);
        if (userId != null) {
            wrapper.eq("uid", userId);
        }
        List<StoreOrderVo> storeOrderVos = baseMapper.selectVoList(wrapper);
        if (CollectionUtils.isEmpty(storeOrderVos)) {
            throw new ServiceException("订单并不存在");
        }
        StoreOrderVo orderDTO = storeOrderVos.get(0);
        List<StoreOrderProductVo> storeOrderProductVoList = orderProductMapper.selectVoList(new QueryWrapper<StoreOrderProduct>().eq("order_id", orderId));
        orderDTO.setProductList(storeOrderProductVoList);
        return orderDTO;
    }

    @Override
    public List<StoreOrderVo> selectListVoByWrapper(QueryWrapper<StoreOrder> wrapper) {
        return baseMapper.selectVoList(wrapper);
    }
}
