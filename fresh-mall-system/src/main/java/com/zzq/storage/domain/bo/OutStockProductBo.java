package com.zzq.storage.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;


/**
 * 出库商品业务对象 kx_out_stock_product
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class OutStockProductBo extends BaseEntity {

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
     * 出库单号
     */
    private String outStockNumbers;

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
     * 出库数量
     */
    private Long outStockNum;

    /**
     * 商品规格id
     */
    private Long productAttrId;


}
