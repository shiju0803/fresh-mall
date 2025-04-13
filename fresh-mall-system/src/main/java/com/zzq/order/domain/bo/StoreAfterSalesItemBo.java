package com.zzq.order.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 售后子业务对象 kx_store_after_sales_item
 * @date 2024-11-23
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreAfterSalesItemBo extends BaseEntity {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 售后id
     */
    private Long storeAfterSalesId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 退货东西的详情信息
     */
    private String cartInfo;

    /**
     * 删除状态
     */
    private Integer isDel;


}
