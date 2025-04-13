package com.zzq.carousel.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;


/**
 * 商铺广告业务对象 kx_carousel
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CarouselBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
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
