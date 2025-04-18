package com.zzq.order.biz;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zzq.common.enums.OrderStatusType;
import com.zzq.common.enums.PayMethodEnum;
import com.zzq.common.enums.UserLoginType;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.StoreOrderProduct;
import com.zzq.order.mapper.StoreOrderMapper;
import com.zzq.order.mapper.StoreOrderProductMapper;
import com.zzq.product.mapper.StoreProductMapper;
import com.zzq.user.domain.User;
import com.zzq.user.mapper.UserMapper;
import com.zzq.wechat.WxPayConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Created by admin on 2019/7/10.
 */
@Service
public class OrderBizService {

    private static final String ORDER_STATUS_LOCK = "ORDER_STATUS_LOCK_";

    //订单退款乐观锁
    public static final String ORDER_REFUND_LOCK = "ORDER_REFUND_LOCK_";

    private static final Logger logger = LoggerFactory.getLogger(OrderBizService.class);

    @Autowired
    private StoreOrderMapper orderMapper;

    @Autowired
    private StoreOrderProductMapper orderProductMapper;

    @Autowired
    private StoreProductMapper storeProductMapper;

    @Autowired
    private UserMapper userMapper;

    public Boolean changeOrderStatus(String orderId, Integer nowStatus, StoreOrder updateOrderDO) {
        try {
            // 防止传入值为空,导致其余订单被改变
            if (orderId == null || updateOrderDO == null) {
                throw new ServiceException("订单状态流转失败！");
            }

            if (orderMapper.update(updateOrderDO,
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


    public Boolean updateOrderStatus(String orderNo, Integer status) throws ServiceException {
        StoreOrder updateStoreOrder = StoreOrder.builder().build();
        updateStoreOrder.setOrderId(orderNo);
        updateStoreOrder.setStatus(status);
        return updateOrder(orderNo, updateStoreOrder);
    }

    public Boolean updateOrder(String orderNo, StoreOrder storeOrder) {
        Lock lock = RedisUtils.lock(ORDER_STATUS_LOCK + orderNo);
        try {
            // 防止传入值为空,导致其余订单被改变
            if (orderNo == null || storeOrder == null) {
                throw new ServiceException("订单状态流转失败！");
            }
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                // kxStoreOrder 是要被更新的数据，orderNo是条件
                if (orderMapper.update(storeOrder, new QueryWrapper<StoreOrder>().eq("order_id", orderNo)) > 0) {
                    return true;
                }
                throw new ServiceException("订单状态流转失败！");
            } else {
                throw new ServiceException("订单系统繁忙~");
            }

        } catch (Exception e) {
            logger.error("[订单状态扭转] 异常", e);
            throw new ServiceException("订单系统未知异常");
        } finally {
            lock.unlock();
        }
    }

    public StoreOrder checkOrderExist(String orderId, Long userId) {
        QueryWrapper<StoreOrder> wrapper = new QueryWrapper<StoreOrder>().eq("order_id", orderId);
        if (userId != null) {
            wrapper.eq("uid", userId);
        }
        List<StoreOrder> storeOrderList = orderMapper.selectList(wrapper);
        if (CollectionUtils.isEmpty(storeOrderList)) {
            throw new ServiceException("订单不存在");
        }
        return storeOrderList.get(0);
    }


    public String groupShopStatusRefund(String orderNo) throws ServiceException {
        Lock lock = RedisUtils.lock(ORDER_REFUND_LOCK + orderNo);
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                //1.校验订单状态是否处于团购状态中
                StoreOrder orderDO = checkOrderExist(orderNo, null);
                if (orderDO.getStatus() != OrderStatusType.GROUP_SHOP_WAIT.getCode()) {
                    throw new ServiceException("订单状态不是团购状态");
                }
                //2.退款处理
                //2.1.1 先流转状态
                StoreOrder updateStoreOrder = StoreOrder.builder().build();
                updateStoreOrder.setStatus(OrderStatusType.REFUNDED.getCode());
                updateStoreOrder.setUpdateTime(new Date());
                changeOrderStatus(orderNo, OrderStatusType.GROUP_SHOP_WAIT.getCode(), updateStoreOrder);
                Long userId = orderDO.getUid();
                User userDO = userMapper.selectById(userId);
                Integer loginType = userDO.getLoginType();

                //2.1.2 向微信支付平台发送退款请求
                WxPayService wxPayService = WxPayConfiguration.getPayService(loginType == UserLoginType.MP_WEIXIN.getCode() ? PayMethodEnum.MINI : PayMethodEnum.APP);
                WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
                wxPayRefundRequest.setOutTradeNo(orderNo);
                wxPayRefundRequest.setOutRefundNo("refund_" + orderNo);
                wxPayRefundRequest.setRefundDesc("团购失败退款");
                BigDecimal bigDecimal = new BigDecimal("100");
                wxPayRefundRequest.setTotalFee(orderDO.getPayPrice().subtract(orderDO.getFreightPrice()).multiply(bigDecimal).intValue());
                wxPayRefundRequest.setRefundFee(orderDO.getPayPrice().subtract(orderDO.getFreightPrice()).multiply(bigDecimal).intValue());
                WxPayRefundResult wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
                if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
                    logger.warn("[微信退款] 失败 : " + wxPayRefundResult.getReturnMsg());
                    throw new ServiceException(wxPayRefundResult.getReturnMsg(), 500);
                }
                if (!wxPayRefundResult.getResultCode().equals("SUCCESS")) {
                    logger.warn("[微信退款] 失败 : " + wxPayRefundResult.getReturnMsg());
                    throw new ServiceException(wxPayRefundResult.getReturnMsg(), 500);
                }
                return "ok";
            } else {
                throw new ServiceException("系统繁忙~");
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            logger.error("[微信退款] 异常", e);
            throw new ServiceException(e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    /**
     * 将冻结库存还回去
     * @param no
     */
    public void callbackStock(String no) {
        StoreOrder storeOrder = orderMapper.selectOne(new LambdaQueryWrapper<StoreOrder>().eq(StoreOrder::getOrderId, no));
        List<StoreOrderProduct> orderId = orderProductMapper.selectList(new QueryWrapper<StoreOrderProduct>().eq("order_id", storeOrder.getId()));
        for (StoreOrderProduct storeOrderProduct : orderId) {
            storeProductMapper.restoreSkuStock(storeOrderProduct.getProductId(), storeOrderProduct.getNum(), storeOrder.getStoreId());
        }
    }

}
