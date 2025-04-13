package com.zzq.web.controller.app.order;

import com.zzq.common.annotation.RateLimiter;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.bo.OrderRequestBo;
import com.zzq.order.domain.vo.StoreOrderVo;
import com.zzq.web.controller.app.order.builder.OrderBuilder;
import com.zzq.web.controller.app.order.builder.OrderDirector;
import com.zzq.web.controller.app.order.service.IAppOrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * app订单
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/order/app")
public class AppOrderController extends BaseAppController {

    private final OrderBuilder orderBuilder;

    private final IAppOrderService appOrderService;

    private static final String TAKE_ORDER_LOCK = "TAKE_ORDER_";


    private static final Logger logger = LoggerFactory.getLogger(AppOrderController.class);

    /**
     * 提交订单
     */
    @PostMapping("/takeOrder")
    @Transactional(rollbackFor = Exception.class)
    @RateLimiter(count = 2)
    public R<String> takeOrder(@RequestBody OrderRequestBo orderRequest) {
        Long userId = getAppLoginUser().getUserId();
        Lock lock = RedisUtils.lock(TAKE_ORDER_LOCK + userId);
        boolean isLocked;
        try {
            isLocked = lock.tryLock(20, TimeUnit.SECONDS);
            if (isLocked) {
                //加上乐观锁，防止用户重复提交订单
                try {
                    StoreOrder orderDO = StoreOrder.builder().build();
                    OrderDirector orderDirector = new OrderDirector(orderBuilder);
                    orderDirector.constructOrder(orderDO, orderRequest, "order.getChannel()", userId);
                    return R.ok(orderDO.getOrderId());
                } catch (ServiceException e) {
                    throw e;
                } catch (Exception e) {
                    logger.error("[提交订单] 异常", e);
                    throw new ServiceException("订单系统未知异常");
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
        throw new ServiceException("订单系统繁忙~");
    }


    /**
     * 微信小程序预先支付
     */
    @GetMapping("/wxPrepay")
    @Transactional(rollbackFor = Exception.class)
    public R<Object> wxPrepay(String orderId) {
        Long userId = getAppLoginUser().getUserId();
        Integer loginType = getAppLoginUser().getLoginType();
        String openId = getAppLoginUser().getOpenId();
        return R.ok(appOrderService.wxPrepay(orderId, userId,loginType,openId));
    }

    @GetMapping("/getOrderPage")
    public TableDataInfo<StoreOrderVo> getOrderPage( @RequestParam(defaultValue = "1") Integer pageNo,
                                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                                       String status){
        Long userId = getAppLoginUser().getUserId();
        return appOrderService.getOrderPage(pageNo, pageSize, status,userId);
    }


    /**
     * 取消订单
     * @return
     */
    @GetMapping("/cancel")
    public R<String> cancel(String orderId){
        Long userId = getAppLoginUser().getUserId();
        return R.ok(appOrderService.cancel(orderId,userId));
    }
    /**
     * 确认订单
     * @return
     */
    @GetMapping("/confirm")
    public R<String> confirm(String orderId){
        Long userId = getAppLoginUser().getUserId();
        return R.ok(appOrderService.confirm(orderId,userId));
    }

    /**
     * 获取订单详情
     * @return
     */
    @GetMapping("/getOrderDetail")
    public R<StoreOrderVo> getOrderDetail(Long orderId){
        Long userId = getAppLoginUser().getUserId();
        return R.ok(appOrderService.getOrderDetail(orderId,userId));
    }

}
