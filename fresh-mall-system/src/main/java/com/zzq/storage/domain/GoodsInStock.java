package com.zzq.storage.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 商品入库对象 kx_goods_in_stock
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_goods_in_stock")
public class GoodsInStock extends BaseEntity {

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
     * 入库单号
     */
    private String inStockNumbers;
    /**
     * 0:待入库;1:已入库；
     */
    private Integer states;
    /**
     * 入库人
     */
    private String ingoingPerson;
    /**
     * 入库时间
     */
    private Date ingoingTime;
    /**
     * 备注
     */
    private String remarks;
    /**
     *
     */
    private String outgoingDay;

}
