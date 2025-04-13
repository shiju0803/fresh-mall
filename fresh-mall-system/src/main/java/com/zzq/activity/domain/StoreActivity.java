package com.zzq.activity.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 活动商品对象 kx_store_activity
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_store_activity")
public class StoreActivity extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 活动id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 活动标题
     */
    private String title;
    /**
     * 活动海报
     */
    private String imgUrl;
    /**
     * 海报链接
     */
    private String outUrl;
    /**
     * 活动详情
     */
    private String description;
    /**
     * 状态(是否开启)
     */
    private Integer status;

}
