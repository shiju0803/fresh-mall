package com.zzq.storage.domain.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzq.common.annotation.ExcelDictFormat;
import com.zzq.common.convert.ExcelDictConvert;
import com.zzq.product.domain.vo.StoreCategoryVo;
import com.zzq.product.domain.vo.StoreProductAttrVo;
import com.zzq.product.domain.vo.StoreProductVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


/**
 * 前置仓商品视图对象 kx_stock
 */
@Data
@ExcelIgnoreUnannotated
public class StockVo {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ExcelProperty(value = "主键")
    private Long id;

    /**
     * 仓库名称
     */
    private String storageName;
    /**
     * 商品id
     */
    @ExcelProperty(value = "商品id")
    private Long productId;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 分类id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 商品条码
     */
    private String barCode;

    /**
     * 商品规格id
     */
    @ExcelProperty(value = "商品规格id")
    private Long productAttrId;

    /**
     * 图片
     */
    private String image;
    /**
     * 商品规格名称
     */
    private String productAttrName;

    /**
     * 前置仓id
     */
    @ExcelProperty(value = "前置仓id")
    private Long storageId;

    /**
     * 销售状态1上架0下架
     */
    @ExcelProperty(value = "销售状态1上架0下架")
    private Integer status;

    /**
     * 库存
     */
    @ExcelProperty(value = "库存")
    private Long stock;

    /**
     * 当前库存
     */
    @ExcelProperty(value = "当前库存")
    private Long nowStock;



    /**
     * 销售量
     */
    @ExcelProperty(value = "销售量")
    private Long sales;

    /**
     * 冻结库存
     */
    @ExcelProperty(value = "冻结库存")
    private Long frezzStock;

    /**
     * 当前售价
     */
    @ExcelProperty(value = "当前售价")
    private BigDecimal price;

    /**
     * 单位
     */
    @ExcelProperty(value = "单位")
    private String unitName;

    /**
     * 预警数量
     */
    @ExcelProperty(value = "预警数量")
    private Long warningNum;

    private Date updateTime;

    private Date createTime;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 仓库对象
     */
    private StorageVo storageVo;

    /**
     * 商品对象
     */
    private StoreProductVo productVo;
    /**
     * 商品对象
     */
    private StoreProductAttrVo productAttrVo;
    /**
     * 商品对象
     */
    private StoreCategoryVo categoryVo;
}
