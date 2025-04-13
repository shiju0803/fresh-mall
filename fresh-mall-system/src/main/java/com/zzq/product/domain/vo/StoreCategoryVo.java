package com.zzq.product.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;


/**
 * 商品分类视图对象 kx_store_category
 */
@Data
@ExcelIgnoreUnannotated
public class StoreCategoryVo {

    private static final long serialVersionUID = 1L;

    /**
     * 商品分类表ID
     */
    private Long id;

    /**
     * 父id
     */
    @ExcelProperty(value = "父id")
    private Long pid;

    /**
     * 分类名称
     */
    @ExcelProperty(value = "分类名称")
    private String cateName;

    /**
     * 排序
     */
    @ExcelProperty(value = "排序")
    private Integer sort;

    /**
     * 图标
     */
    @ExcelProperty(value = "图标")
    private String pic;

    /**
     * 是否推荐
     */
    @ExcelProperty(value = "是否推荐")
    private Integer isShow;

    /**
     * 删除状态
     */
    private Integer isDel;

    private String label;


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<StoreCategoryVo> children;

}
