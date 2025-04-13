package com.zzq.activity.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * 活动商品对象 kx_store_activity_product
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_activity_product")
public class StoreActivityProduct extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 商品id
     */
    private Long productId;
    /**
     * 活动id
     */
    private Long activityId;
    /**
     * 活动价
     */
    private BigDecimal activityPrice;



}
