package com.zzq.web.controller.app.storage.service;

import com.zzq.common.core.page.TableDataInfo;
import com.zzq.recommend.domain.vo.RecommendVo;
import com.zzq.storage.domain.vo.IntegralIndexDataVo;
import com.zzq.storage.domain.vo.RecentlyStorageVo;
import com.zzq.storage.domain.vo.StorageVo;

import java.math.BigDecimal;

public interface IAppStorageService {

    /**
     * 获取最近的仓库的数据
     * @param longitude
     * @param latitude
     * @return
     */
    RecentlyStorageVo getRecentlyStorage(BigDecimal longitude, BigDecimal latitude);


    /**
     * 获取指定仓库数据内容
     */
    IntegralIndexDataVo getIndexDataByStorage(Long storageId);

    /**
     * 获取仓库信息
     * @param storageId
     * @return
     */
    StorageVo getStorage(Long storageId);

    /**
     * 获取最近的仓库推荐内容
     * @param storageId
     * @param recommedType
     * @param pageNo
     * @param pageSize
     * @return
     */
    TableDataInfo<RecommendVo> getRecommendByStorage(Long storageId, Integer recommedType, Integer pageNo, Integer pageSize);
}
