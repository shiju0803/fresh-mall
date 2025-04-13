package com.zzq.web.controller.admin.carousel.service;

import com.zzq.carousel.domain.bo.CarouselBo;
import com.zzq.carousel.domain.vo.CarouselVo;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 商铺广告Service接口
 */
public interface ICarouselService {

    /**
     * 查询商铺广告
     */
    CarouselVo queryById(Long id);

    /**
     * 查询商铺广告列表
     */
    TableDataInfo<CarouselVo> queryPageList(CarouselBo bo, PageQuery pageQuery);

    /**
     * 查询商铺广告列表
     */
    List<CarouselVo> queryList(CarouselBo bo);

    /**
     * 新增商铺广告
     */
    Boolean insertByBo(CarouselBo bo);

    /**
     * 修改商铺广告
     */
    Boolean updateByBo(CarouselBo bo);

    /**
     * 校验并批量删除商铺广告信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);
}
