package com.zzq.storage.domain.bo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.core.validate.EditGroup;
import com.zzq.storage.domain.vo.InStockProductVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * 商品入库业务对象 kx_goods_in_stock
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsInStockBo extends BaseEntity {

    /**
     * 出库id
     */
    @NotNull(message = "出库id不能为空", groups = {EditGroup.class})
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate ingoingTime;

    /**
     * 备注
     */
    private String remarks;

    /**
     *
     */
    private String outgoingDay;

    /**
     * 仓库ids
     */
    private Set<Long> storageIds;

    private List<InStockProductVo> inStockProductVoList;
}
