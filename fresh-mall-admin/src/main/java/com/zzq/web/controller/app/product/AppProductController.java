package com.zzq.web.controller.app.product;

import cn.dev33.satoken.annotation.SaIgnore;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.common.enums.DeviceType;
import com.zzq.common.helper.LoginHelper;
import com.zzq.product.domain.vo.StoreCategoryVo;
import com.zzq.product.domain.vo.StoreProductVo;
import com.zzq.web.controller.app.product.service.IAppCategoryService;
import com.zzq.web.controller.app.product.service.IAppProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app商品管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/app")
public class AppProductController extends BaseAppController {

    private final IAppProductService appProductService;
    private final IAppCategoryService appCategoryService;

    /**
     * 指定仓库下获取商品详情
     */
    @SaIgnore
    @GetMapping("/getGoodsByStorage")
    public R<StoreProductVo> getGoodsByStorage(Long storageId, Long productId) {
        Long userId = 0L;
        DeviceType deviceType = LoginHelper.getDeviceType();
        if (!ObjectUtils.isEmpty(deviceType)) {
            userId = getAppLoginUser().getUserId();
        }
        return R.ok(appProductService.getGoodsByStorage(storageId, productId, userId));
    }

    /**
     * 查询商品分类列表
     */
    @SaIgnore
    @GetMapping("/categoryList")
    public R<List<StoreCategoryVo>> categoryList() {
        return R.ok(appCategoryService.categoryList());
    }
}
