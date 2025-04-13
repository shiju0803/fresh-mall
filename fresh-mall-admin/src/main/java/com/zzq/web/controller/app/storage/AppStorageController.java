package com.zzq.web.controller.app.storage;

import cn.dev33.satoken.annotation.SaIgnore;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.product.domain.vo.StoreProductVo;
import com.zzq.recommend.domain.vo.RecommendVo;
import com.zzq.storage.domain.vo.IntegralIndexDataVo;
import com.zzq.storage.domain.vo.RecentlyStorageVo;
import com.zzq.storage.domain.vo.StorageVo;
import com.zzq.web.controller.app.product.service.IAppProductService;
import com.zzq.web.controller.app.storage.service.IAppStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * 仓库管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/position")
public class AppStorageController extends BaseAppController {

    private final IAppStorageService iAppStorageService;

    private final IAppProductService appProductService;

    /**
     * 获取仓库信息
     */
    @GetMapping("/getStorage")
    public R<StorageVo> getStorage(Long storageId) {
        return R.ok(iAppStorageService.getStorage(storageId));
    }

    /**
     * 获取最近的仓库
     */
    @SaIgnore
    @GetMapping("/getRecentlyStorage")
    public R<RecentlyStorageVo> getRecentlyStorage(BigDecimal lng, BigDecimal lat) {
        return R.ok(iAppStorageService.getRecentlyStorage(lng, lat));
    }

    /**
     * 获取最近的仓库推荐内容
     */
    @SaIgnore
    @GetMapping("/getRecommendByStorage")
    public R<TableDataInfo<RecommendVo>> getRecommendByStorage(Long storageId,
                                                                 Integer recommendType,
                                                                 @RequestParam(defaultValue = "1") Integer pageNo,
                                                                 @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok(iAppStorageService.getRecommendByStorage(storageId, recommendType,pageNo,pageSize));
    }

    /**
     * 获取最近的仓库分页商品
     */
    @SaIgnore
    @GetMapping("/getGoodsPageByStorage")
    public R<TableDataInfo<StoreProductVo>> getGoodsPageByStorage(Long storageId,
                                                                    @RequestParam(defaultValue = "1") Integer pageNo,
                                                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                                                    Long categoryId,
                                                                    String orderBy,
                                                                    Boolean isAsc,
                                                                    String title) {
        TableDataInfo<StoreProductVo> storage = appProductService.getGoodsPageByStorage(storageId, pageNo, pageSize, categoryId, orderBy, isAsc, title,0);
        return R.ok(storage);
    }

    /**
     * 获取指定仓库数据内容
     */
    @SaIgnore
    @GetMapping("/getIndexDataByStorage")
    public R<IntegralIndexDataVo> getIndexDataByStorage(Long storageId) {
        return R.ok(iAppStorageService.getIndexDataByStorage(storageId));
    }
}
