package com.zzq.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 足迹对象 kx_user_footprint
 * @date 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_user_footprint")
public class UserFootprint extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
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
