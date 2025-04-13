package com.zzq.product.domain;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 商品规格对象 kx_store_product_rule
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value="kx_store_product_rule",autoResultMap = true)
public class StoreProductRule extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     *
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 规格名称
     */
    private String ruleName;
    /**
     * 规格值
     */
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private JSONArray ruleValue;
    /**
     *
     */
    private Integer isDel;

}
