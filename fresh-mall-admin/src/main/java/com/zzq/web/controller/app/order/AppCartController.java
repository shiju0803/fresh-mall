package com.zzq.web.controller.app.order;

import cn.dev33.satoken.annotation.SaIgnore;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.common.enums.DeviceType;
import com.zzq.common.helper.LoginHelper;
import com.zzq.order.domain.vo.StoreCartVo;
import com.zzq.web.controller.app.order.service.IAppCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app购物车
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/cart/app")
public class AppCartController extends BaseAppController {


    private final IAppCartService kxAppCartService;


    /**
     * 获取购物车数量
     */
    @GetMapping("/countCart")
    @SaIgnore
    public R<Long> countCart(Long storageId) {
        Long userId = 0L;
        DeviceType deviceType = LoginHelper.getDeviceType();
        if (!ObjectUtils.isEmpty(deviceType)) {
            userId = getAppLoginUser().getUserId();
        }
        return R.ok(kxAppCartService.countCart(storageId, userId));
    }

    /**
     * 获取购物车商品列表
     */
    @GetMapping("/getCartList")
    @SaIgnore
    public R<List<StoreCartVo>> getCartList(Long storageId) {
        Long userId = 0L;
        DeviceType deviceType = LoginHelper.getDeviceType();
        if (!ObjectUtils.isEmpty(deviceType)) {
            userId = getAppLoginUser().getUserId();
        }
        return R.ok(kxAppCartService.getCartList(storageId, userId));
    }


    /**
     * 获取购物车商品列表
     */
    @GetMapping("/addCartItem")
    public R<StoreCartVo> addCartItem(Long productId, Long num) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxAppCartService.addCartItem(productId, num, userId));
    }


    /**
     * 更新购物车数量
     */
    @GetMapping("/updateCartItemNum")
    public R<Long> updateCartItemNum(Long cartId, Long num) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxAppCartService.updateCartItemNum(cartId, num, userId));
    }

    /**
     * 将购物车商品删除
     */
    @GetMapping("/removeCartItem")
    public R<Boolean> removeCartItem(Long cartId) {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxAppCartService.removeCartItem(cartId, userId));
    }
}
