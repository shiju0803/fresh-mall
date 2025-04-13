package com.zzq.group.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.group.domain.GroupShopProduct;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 团购业务对象 kx_group_shop
 * @date 2023-10-07
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class GroupShopBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private Long productId;

    /**
     *
     */
    private BigDecimal minPrice;

    /**
     *
     */
    private BigDecimal maxPrice;

    /**
     * 团购开始时间
     */
    private Date startTime;

    /**
     * 团购结束时间
     */
    private Date endTime;

    /**
     * 团购基础人数
     */
    private Long minimumNumber;

    /**
     * 团购已经购买人数
     */
    private Long alreadyBuyNumber;

    /**
     * 团购结束时购买人数未达到基础人数,是否自动退款
     */
    private Integer automaticRefund;

    /**
     * 判断团购商品是否在活动期间
     */
    private Integer status;

    /**
     *
     */
    private Long storageId;


    private List<GroupShopProduct> groupShopSkuList;

    /**
     * 仓库权限参数
     */
    private Set<Long> storageIds;
}
