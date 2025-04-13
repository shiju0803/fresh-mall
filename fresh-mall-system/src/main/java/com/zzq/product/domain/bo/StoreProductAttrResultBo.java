package com.zzq.product.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品属性详情业务对象 kx_store_product_attr_result
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreProductAttrResultBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 商品ID
     */
    private Long productId;

    /**
     * 商品属性参数
     */
    private String result;

    /**
     * 上次修改时间
     */
    private Date changeTime;


}
