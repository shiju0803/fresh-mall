package com.zzq.product.domain.vo;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;


/**
 * 商品属性详情视图对象 kx_store_product_attr_result
 */
@Data
@ExcelIgnoreUnannotated
public class StoreProductAttrResultVo {

    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @ExcelProperty(value = "")
    private Long id;

    /**
     * 商品ID
     */
    @ExcelProperty(value = "商品ID")
    private Long productId;

    /**
     * 商品属性参数
     */
    @ExcelProperty(value = "商品属性参数")
    private String result;

    /**
     * 上次修改时间
     */
    @ExcelProperty(value = "上次修改时间")
    private Date changeTime;


}
