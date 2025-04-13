package com.zzq.notify;

import com.alibaba.fastjson.JSONObject;
import com.zzq.order.domain.vo.StoreOrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: 为了能够正常启动demo的mock实现
 * User: admin
 * Date: 2022/12/27
 * Time: 16:22
 */
public class MockMemberNotifyBizServiceImpl implements MemberNotifyBizService {

    private static final Logger logger = LoggerFactory.getLogger(MockMemberNotifyBizServiceImpl.class);

    @Override
    public void newOrder(StoreOrderVo orderDTO) {
        logger.info("[mock通知 有新订单] 下单通知：" + JSONObject.toJSONString(orderDTO));
    }

    @Override
    public void refundOrder(StoreOrderVo orderDTO) {
        logger.info("[mock通知 有新退款] 退款通知：" + JSONObject.toJSONString(orderDTO));
    }

    @Override
    public void completeOrder(StoreOrderVo orderDTO) {
        logger.info("[mock通知 订单完成] 订单通知：" + JSONObject.toJSONString(orderDTO));
    }
}
