package com.zzq.web.controller.app.recommend.service;

import com.zzq.common.core.domain.entity.SysDictData;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.recommend.domain.vo.RecommendVo;

import java.util.List;

public interface IAppRecommendService {

    TableDataInfo<RecommendVo> getRecommendByType(Long storageId, Integer recommendType, Integer pageNo, Integer pageSize);

    /**
     * 获取枚举
     *
     * @return
     */
    List<SysDictData> getRecommendTypeEnums();

}
