package com.zzq.web.controller.admin.carousel.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.carousel.domain.Carousel;
import com.zzq.carousel.domain.bo.CarouselBo;
import com.zzq.carousel.domain.vo.CarouselVo;
import com.zzq.carousel.mapper.CarouselMapper;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.web.controller.admin.carousel.service.ICarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.zzq.common.constant.ProductCacheConstants.ADVERTISEMENT_NAME;

/**
 * 商铺广告Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class CarouselServiceImpl implements ICarouselService {

    private final CarouselMapper baseMapper;

    /**
     * 查询商铺广告
     */
    @Override
    public CarouselVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商铺广告列表
     */
    @Override
    public TableDataInfo<CarouselVo> queryPageList(CarouselBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Carousel> lqw = buildQueryWrapper(bo);
        Page<CarouselVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商铺广告列表
     */
    @Override
    public List<CarouselVo> queryList(CarouselBo bo) {
        LambdaQueryWrapper<Carousel> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Carousel> buildQueryWrapper(CarouselBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Carousel> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getAdType() != null, Carousel::getAdType, bo.getAdType());
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), Carousel::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getUrl()), Carousel::getUrl, bo.getUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getImgUrl()), Carousel::getImgUrl, bo.getImgUrl());
        lqw.eq(bo.getStatus() != null, Carousel::getStatus, bo.getStatus());
        lqw.eq(StringUtils.isNotBlank(bo.getOutUrl()), Carousel::getOutUrl, bo.getOutUrl());
        return lqw;
    }

    /**
     * 新增商铺广告
     */
    @Override
    public Boolean insertByBo(CarouselBo bo) {
        Date now = new Date();
        boolean urlEmntyFlag = false;
        if (StringUtils.isEmpty(bo.getUrl()) && StringUtils.isEmpty(bo.getOutUrl())) {
            urlEmntyFlag = true;
        }
        if (urlEmntyFlag) {
            throw new ServiceException("广告URL不能为空");
        }
        Carousel add = BeanUtil.toBean(bo, Carousel.class);
        add.setCreateTime(now);
        add.setUpdateTime(now);
        if (baseMapper.insert(add) > 0) {
            RedisUtils.deleteKeys(ADVERTISEMENT_NAME + "*");
            return true;
        }
        throw new ServiceException("添加广告数据库失败");
    }

    /**
     * 修改商铺广告
     */
    @Override
    public Boolean updateByBo(CarouselBo bo) {
        Date now = new Date();
        boolean urlEmntyFlag = false;
        if (StringUtils.isEmpty(bo.getUrl()) && StringUtils.isEmpty(bo.getOutUrl())) {
            urlEmntyFlag = true;
        }
        if (urlEmntyFlag) {
            throw new ServiceException("广告URL不能为空");
        }
        Carousel update = BeanUtil.toBean(bo, Carousel.class);
        update.setUpdateTime(now);
        if (baseMapper.updateById(update) > 0) {
            RedisUtils.deleteKeys(ADVERTISEMENT_NAME + "*");
            return true;
        }
        throw new ServiceException("修改广告数据库失败");

    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Carousel entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商铺广告
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        if (baseMapper.deleteBatchIds(ids) > 0) {
            RedisUtils.deleteKeys(ADVERTISEMENT_NAME + "*");
            return true;
        }
        throw new ServiceException("删除广告数据库失败");
    }
}
