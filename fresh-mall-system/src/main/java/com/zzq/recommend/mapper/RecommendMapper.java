package com.zzq.recommend.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.mapper.BaseMapperPlus;
import com.zzq.recommend.domain.Recommend;
import com.zzq.recommend.domain.vo.RecommendVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 推荐管理Mapper接口
 */
public interface RecommendMapper extends BaseMapperPlus<RecommendMapper, Recommend, RecommendVo> {

    /**
     * 获取指定仓库推荐下的商品
     *
     * @param storageId
     * @param recommendType
     * @param offset
     * @param size
     * @return
     */
    List<RecommendVo> getRecommendByStorage(@Param("storageId") Long storageId, @Param("recommendType") Integer recommendType, @Param("offset") Integer offset, @Param("size") Integer size);

    Long getRecommendByStorageCount(@Param("storageId") Long storageId, @Param("recommendType") Integer recommendType);

    Page<RecommendVo> selectVoPageBySQL(@Param("page") Page<Recommend> page, @Param(Constants.WRAPPER) Wrapper<Recommend> lqw);
}
