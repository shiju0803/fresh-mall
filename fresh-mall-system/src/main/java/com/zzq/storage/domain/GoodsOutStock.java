package com.zzq.storage.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品出库对象 kx_goods_out_stock
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_goods_out_stock")
public class GoodsOutStock extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 出库id
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 仓库id
     */
    private Long storageId;
    /**
     * 出库单号
     */
    private String outStockNumbers;
    /**
     * 0:待出库;1:已出库；
     */
    private Integer states;
    /**
     * 出库人
     */
    private String outgoingPerson;
    /**
     * 出库时间
     */
    private Date outgoingTime;
    /**
     * 备注
     */
    private String remarks;
    /**
     *
     */
    private String outgoingDay;

}
