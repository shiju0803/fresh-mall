package com.zzq.storage.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.core.validate.AddGroup;
import com.zzq.common.core.validate.EditGroup;
import com.zzq.storage.domain.vo.OutStockProductVo;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 商品出库业务对象 kx_goods_out_stock
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class GoodsOutStockBo extends BaseEntity {

    /**
     * 出库id
     */
    @NotNull(message = "出库id不能为空", groups = { EditGroup.class })
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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate outgoingTime;

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


    private List<OutStockProductVo> outStockProductVoList;
}
