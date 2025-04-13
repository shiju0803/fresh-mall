package com.zzq.group.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;


/**
 * 团购商品对象 kx_group_shop_product
 * @date 2023-10-07
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_group_shop_product")
public class GroupShopProduct extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 团购商品
     */
    private Long productAttrId;
    /**
     * 团购主键
     */
    private Long groupShopId;
    /**
     * 团购价格
     */
    private BigDecimal productGroupShopPrice;

}
