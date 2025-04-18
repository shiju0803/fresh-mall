package com.zzq.storage.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;
import java.util.Set;


/**
 * 入库商品业务对象 kx_in_stock_product
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class InStockProductBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 商品类目
     */
    private String categoryName;

    /**
     * 商品条码
     */
    private String barCode;

    /**
     * 入库单号
     */
    private String inStockNumbers;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品规格
     */
    private String productAttrName;

    /**
     * 库存可用量
     */
    private Long stock;

    /**
     * 入库数量
     */
    private Long inStockNum;

    /**
     * 商品规格id
     */
    private Long productAttrId;

    /**
     * 仓库ids
     */
    private Set<Long> storageIds;
}
