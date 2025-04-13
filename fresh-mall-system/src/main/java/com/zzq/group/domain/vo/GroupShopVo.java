package com.zzq.group.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzq.common.annotation.ExcelDictFormat;
import com.zzq.common.convert.ExcelDictConvert;
import com.zzq.group.domain.GroupShopProduct;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * 团购视图对象 kx_group_shop
 * @date 2023-10-07
 */
@Data
@ExcelIgnoreUnannotated
public class GroupShopVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long productId;

    /**
     *
     */
    @ExcelProperty(value = "")
    private BigDecimal minPrice;

    /**
     *
     */
    @ExcelProperty(value = "")
    private BigDecimal maxPrice;

    /**
     * 团购开始时间
     */
    @ExcelProperty(value = "团购开始时间")
    private Date startTime;

    /**
     * 团购结束时间
     */
    @ExcelProperty(value = "团购结束时间")
    private Date endTime;

    /**
     * 团购基础人数
     */
    @ExcelProperty(value = "团购基础人数")
    private Long minimumNumber;

    /**
     * 团购已经购买人数
     */
    @ExcelProperty(value = "团购已经购买人数")
    private Long alreadyBuyNumber;

    /**
     * 团购结束时购买人数未达到基础人数,是否自动退款
     */
    @ExcelProperty(value = "团购结束时购买人数未达到基础人数,是否自动退款")
    private Integer automaticRefund;

    /**
     * 判断团购商品是否在活动期间
     */
    @ExcelProperty(value = "判断团购商品是否在活动期间", converter = ExcelDictConvert.class)
    @ExcelDictFormat(dictType = "group_status")
    private Integer status;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long storageId;


    private List<GroupShopProduct> groupShopSkuList;



    /**
     * spu属性
     */
    private BigDecimal otPrice;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private String storeName;

    private Integer sales;

    private String image;

    private String unitName;

    private String storageName;

}
