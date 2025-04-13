package com.zzq.carousel.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzq.common.annotation.ExcelDictFormat;
import com.zzq.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 商铺广告视图对象 kx_carousel
 */
@Data
@ExcelIgnoreUnannotated
public class CarouselVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 广告类型
     */
    @ExcelProperty(value = "广告类型")
    private String adType;

    /**
     * 广告标题
     */
    @ExcelProperty(value = "广告标题")
    private String title;

    /**
     * 广告请求路径
     */
    @ExcelProperty(value = "广告请求路径")
    private String url;

    /**
     * 图片路径
     */
    @ExcelProperty(value = "图片路径")
    private String imgUrl;

    /**
     * 状态
     */
    @ExcelProperty(value = "状态")
    private String status;

    /**
     * 站外链接
     */
    @ExcelProperty(value = "站外链接")
    private String outUrl;

    /**
     * 附加广告数据
     */
    private Object data;

    /**
     * 排序
     */
    private Integer sort;
}
