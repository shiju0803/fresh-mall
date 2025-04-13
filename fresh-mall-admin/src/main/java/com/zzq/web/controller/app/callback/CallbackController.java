package com.zzq.web.controller.app.callback;

import cn.dev33.satoken.annotation.SaIgnore;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.zzq.common.enums.OrderStatusType;
import com.zzq.common.enums.PayMethodEnum;
import com.zzq.executor.GlobalExecutor;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.StoreOrderProduct;
import com.zzq.order.domain.vo.StoreOrderProductVo;
import com.zzq.order.domain.vo.StoreOrderVo;
import com.zzq.order.mapper.StoreOrderProductMapper;
import com.zzq.product.mapper.StoreProductMapper;
import com.zzq.web.controller.app.order.service.IAppOrderService;
import com.zzq.wechat.WxPayConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author admin
 * @date 2023/3/10
 */
@RestController
@RequestMapping("/cb")
public class CallbackController {


    @Autowired
    private IAppOrderService appOrderService;

    @Autowired
    private StoreOrderProductMapper orderProductMapper;

    @Autowired
    private StoreProductMapper storeProductMapper;

//    @Autowired
//    private GroupShopMapper groupShopMapper;

//    @Autowired
//    private AdminNotifyBizService adminNotifyBizService;

//    @Autowired
//    private AdminPrintBizService adminPrintBizService;

    private static final Logger logger = LoggerFactory.getLogger(CallbackController.class);

    @RequestMapping("/wxpay")
    @SaIgnore
    @Transactional(rollbackFor = Exception.class)
    public Object wxpay(@RequestBody String body) throws Exception {
//        ============微信支付回调代码 prod 开始============
        WxPayOrderNotifyResult result;
        try {
            WxPayService wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.H5);
            if(wxPayService == null) {
                wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.MINI);
            }
            if(wxPayService == null) {
                wxPayService = WxPayConfiguration.getPayService(PayMethodEnum.APP);
            }
            result = wxPayService.parseOrderNotifyResult(body);
        } catch (WxPayException e) {
            logger.error("[微信解析回调请求] 异常", e);
            return WxPayNotifyResponse.fail(e.getMessage());
        }
        logger.info("处理腾讯支付平台的订单支付");
        logger.info(JSONObject.toJSONString(result));
//        ============微信支付回调代码 prod 结束============

//        ============微信支付回调代码 dev 开始============
//        WxPayOrderNotifyResult result = JSONObject.parseObject(body, WxPayOrderNotifyResult.class);
//        ============微信支付回调代码 dev 结束============
        /* 之前传过去的我们系统的订单ID */
        String orderNo = result.getOutTradeNo();
        String payId = result.getTransactionId();

        List<StoreOrderVo> StoreOrderList = appOrderService.selectListVoByWrapper(
            new QueryWrapper<StoreOrder>()
                .eq("order_id", orderNo));

        if (CollectionUtils.isEmpty(StoreOrderList)) {
            return WxPayNotifyResponse.fail("订单不存在 orderNo=" + orderNo);
        }

        StoreOrderVo order = StoreOrderList.get(0);

        // 检查这个订单是否已经处理过
        if (order.getStatus() != OrderStatusType.UNPAY.getCode()) {
            return WxPayNotifyResponse.success("订单已经处理成功!");
        }

        Integer totalFee = result.getTotalFee();

        // 检查支付订单金额
        if (!totalFee.equals(order.getPayPrice().multiply(new BigDecimal(100)).intValue())) {
            return WxPayNotifyResponse.fail(order.getOrderId() + " : 支付金额不符合 totalFee=" + totalFee);
        }

        //**************** 在此之前都没有 数据库修改 操作 所以前面是直接返回错误的 **********************//

        StoreOrder updateOrderDO = StoreOrder.builder().build();
        updateOrderDO.setPayId(payId);
        updateOrderDO.setPayChannel("WX");
        updateOrderDO.setPayPrice(BigDecimal.valueOf(totalFee));
        updateOrderDO.setPayTime(new Date());
        updateOrderDO.setUpdateTime(order.getPayTime());
        updateOrderDO.setStatus(OrderStatusType.WAIT_PREPARE_GOODS.getCode());
        appOrderService.changeOrderStatus(orderNo, OrderStatusType.UNPAY.getCode(), updateOrderDO);

        //扣款

        List<StoreOrderProductVo> orderProducts = orderProductMapper.selectVoList(new QueryWrapper<StoreOrderProduct>().eq("order_id", order.getId()));
        order.setProductList(orderProducts);
        orderProducts.forEach(item -> {
            //增加销量
            storeProductMapper.incSales(item.getProductId(), item.getNum());
        });

        //通知管理员发货
        GlobalExecutor.execute(() -> {
            //adminNotifyBizService.newOrder(order);
            //adminPrintBizService.newOrderPrint(order);
        });

        return WxPayNotifyResponse.success("支付成功");
    }
}
