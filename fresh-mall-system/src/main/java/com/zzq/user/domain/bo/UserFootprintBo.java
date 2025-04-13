package com.zzq.user.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 足迹业务对象 kx_user_footprint
 * @date 2023-04-06
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class UserFootprintBo extends BaseEntity {

    /**
     *
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;


}
