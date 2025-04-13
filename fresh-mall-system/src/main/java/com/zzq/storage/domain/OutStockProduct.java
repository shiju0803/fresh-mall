package com.zzq.storage.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 出库商品对象 kx_out_stock_product
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_out_stock_product")
public class OutStockProduct extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商品类目
     */
    private String categoryName;
    /**
     * 商品条码
     */
    private String barCode;
    /**
     * 出库单号
     */
    private String outStockNumbers;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品规格
     */
    private String productAttrName;
    /**
     * 库存可用量
     */
    private Long stock;
    /**
     * 出库数量
     */
    private Long outStockNum;
    /**
     * 商品规格id
     */
    private Long productAttrId;

}
