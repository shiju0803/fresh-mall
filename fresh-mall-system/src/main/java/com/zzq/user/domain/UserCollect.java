package com.zzq.user.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 客户收藏对象 kx_user_collect
 * @date 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_user_collect")
public class UserCollect extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 主键
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
