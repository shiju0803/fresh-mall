package com.zzq.coupon.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 优惠券前台用户领取记录业务对象 kx_store_coupon_issue_user
 * @date 2023-02-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StoreCouponIssueUserBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 领取优惠券用户ID
     */
    private Long uid;

    /**
     * 优惠券前台领取ID
     */
    private Integer issueCouponId;

    /**
     *
     */
    private Integer isDel;


}
