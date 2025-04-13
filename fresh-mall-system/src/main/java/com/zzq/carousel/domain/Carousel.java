package com.zzq.carousel.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商铺广告对象 kx_carousel
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_carousel")
public class Carousel extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 广告类型
     */
    private String adType;
    /**
     * 广告标题
     */
    private String title;
    /**
     * 广告请求路径
     */
    private String url;
    /**
     * 图片路径
     */
    private String imgUrl;
    /**
     * 状态
     */
    private String status;
    /**
     * 站外链接
     */
    private String outUrl;

    /**
     * 排序
     */
    private Integer sort;

}
