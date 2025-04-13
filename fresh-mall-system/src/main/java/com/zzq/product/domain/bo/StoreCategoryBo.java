package com.zzq.product.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;


/**
 * 商品分类业务对象 kx_store_category
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StoreCategoryBo extends BaseEntity {

    /**
     * 商品分类表ID
     */
    private Long id;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 父ids
     */
    private List<Long> pids;

    /**
     * 分类名称
     */
    private String cateName;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 图标
     */
    private String pic;

    /**
     * 是否推荐
     */
    private Integer isShow;

    /**
     * 删除状态
     */
    private Integer isDel;
}
