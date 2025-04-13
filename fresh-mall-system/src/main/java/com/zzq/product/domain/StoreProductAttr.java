package com.zzq.product.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商品属性对象 kx_store_product_attr
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_product_attr")
@Builder
public class StoreProductAttr extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 属性值
     */
    private String attrValues;
    /**
     * 删除状态
     */
    private Integer isDel;

}
