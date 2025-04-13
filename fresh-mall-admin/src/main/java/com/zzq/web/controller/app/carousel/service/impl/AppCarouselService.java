package com.zzq.web.controller.app.carousel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzq.carousel.domain.Carousel;
import com.zzq.carousel.mapper.CarouselMapper;
import com.zzq.common.enums.StatusType;
import com.zzq.web.controller.app.carousel.service.IAppCarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AppCarouselService implements IAppCarouselService {

    private final CarouselMapper baseMapper;



    @Override
    public List<Carousel> listAll(Integer adType) {
//        List<Carousel> cacheList = RedisUtils.getCacheList(ADVERTISEMENT_NAME + adType);
        List<Carousel> cacheList = new ArrayList<>();
        if (CollectionUtils.isEmpty(cacheList)) {
            LambdaQueryWrapper<Carousel> wrapper = new LambdaQueryWrapper<Carousel>()
                .eq(Carousel::getStatus, StatusType.ACTIVE.getCode());
            if (adType != 99) {
                wrapper.eq(Carousel::getAdType, adType);
            }
            wrapper.orderByDesc(Carousel::getSort);
            cacheList = baseMapper.selectList(wrapper);
//            RedisUtils.setCacheList(ADVERTISEMENT_NAME + adType, cacheList);
        }
        return cacheList;
    }
}
