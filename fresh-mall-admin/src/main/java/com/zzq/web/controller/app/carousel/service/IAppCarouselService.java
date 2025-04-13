package com.zzq.web.controller.app.carousel.service;

import com.zzq.carousel.domain.Carousel;

import java.util.List;

public interface IAppCarouselService {

    /**
     * 查询所有的广告内容
     *
     * @return
     */
    List<Carousel> listAll(Integer adType);

}
