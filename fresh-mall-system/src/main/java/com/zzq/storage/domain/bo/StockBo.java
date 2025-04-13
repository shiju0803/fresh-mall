package com.zzq.storage.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;


/**
 * 前置仓商品业务对象 kx_stock
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StockBo extends BaseEntity {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 商品id
     */
    private Long productId;
    /**
     * 统一查询
     */
    private String keyword;

    /**
     * 商品规格id
     */
    private Long productAttrId;

    /**
     * 前置仓id
     */
    private Long storageId;

    /**
     * 销售状态1上架0下架
     */
    private Integer status;

    /**
     * 库存
     */
    private Long stock;

    /**
     * 销售量
     */
    private Long sales;

    /**
     * 冻结库存
     */
    private Long frezzStock;

    /**
     * 当前售价
     */
    private BigDecimal price;

    /**
     * 预警数量
     */
    private Long warningNum;

    /**
     * 分类
     */
    private Long categoryId;
    /**
     * 去除商品部分
     */
    private List<Long> notIds;

    /**
     * 仓库权限参数
     */
    private Set<Long> storageIds;
}
