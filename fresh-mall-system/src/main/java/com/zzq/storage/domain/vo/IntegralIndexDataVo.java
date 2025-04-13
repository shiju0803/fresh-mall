package com.zzq.storage.domain.vo;

import com.zzq.carousel.domain.vo.CarouselVo;
import com.zzq.product.domain.vo.StoreProductVo;
import com.zzq.recommend.domain.vo.RecommendVo;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 首页聚合接口DTO
 * @date 2022/7/14
 */
@Data
public class IntegralIndexDataVo {

    private Map<String, List<CarouselVo>> carouseList;

    private List<RecommendVo> cheapRecommend;

    private List<RecommendVo> salesTop;

    private List<StoreProductVo> newTop;

    /**
     * 新鲜时报
     */
    private String newTimesContent;

}
