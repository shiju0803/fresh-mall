package com.zzq.web.controller.app.product.service;

import com.zzq.common.core.page.TableDataInfo;
import com.zzq.product.domain.vo.StoreProductVo;

public interface IAppProductService {

    /**
     * 获取商品列表
     * @param pageNo
     * @param pageSize
     * @param categoryId
     * @param orderBy
     * @param isAsc
     * @param title
     * @return
     */
    TableDataInfo<StoreProductVo> getGoodsPage(Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title);

    /**
     * 获取仓库商品列表
     * @param storageId
     * @param pageNo
     * @param pageSize
     * @param categoryId
     * @param orderBy
     * @param isAsc
     * @param title
     * @return
     */
    TableDataInfo<StoreProductVo> getGoodsPageByStorage(Long storageId, Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title,Integer type);

    /**
     * 指定仓库下获取商品详情
     * @param storageId
     * @param userId
     * @return
     */
    StoreProductVo getGoodsByStorage(Long storageId,Long productId, Long userId);
}
