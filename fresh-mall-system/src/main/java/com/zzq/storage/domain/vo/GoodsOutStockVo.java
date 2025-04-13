package com.zzq.storage.domain.vo;

import java.util.Date;
import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzq.common.annotation.ExcelDictFormat;
import com.zzq.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 商品出库视图对象 kx_goods_out_stock
 */
@Data
@ExcelIgnoreUnannotated
public class GoodsOutStockVo {

    private static final long serialVersionUID = 1L;

    /**
     * 出库id
     */
    @ExcelProperty(value = "出库id")
    private Long id;

    /**
     * 仓库名称
     */
    @ExcelProperty(value = "仓库名称")
    private String storageName;

    /**
     * 仓库id
     */
    @ExcelProperty(value = "仓库id")
    private Long storageId;

    /**
     * 出库单号
     */
    @ExcelProperty(value = "出库单号")
    private String outStockNumbers;

    /**
     * 0:待出库;1:已出库；
     */
    @ExcelProperty(value = "0:待出库;1:已出库；")
    private Integer states;

    /**
     * 出库人
     */
    @ExcelProperty(value = "出库人")
    private String outgoingPerson;

    /**
     * 出库时间
     */
    @ExcelProperty(value = "出库时间")
    private Date outgoingTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remarks;

    /**
     *
     */
    private String outgoingDay;

    private Date updateTime;

    private Date createTime;

    private String createBy;

    private List<OutStockProductVo> outStockProductVoList;

}
