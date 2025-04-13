package com.zzq.storage.domain.vo;

import java.util.Date;
import java.util.List;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.zzq.common.annotation.ExcelDictFormat;
import com.zzq.common.convert.ExcelDictConvert;
import lombok.Data;


/**
 * 商品入库视图对象 kx_goods_in_stock
 */
@Data
@ExcelIgnoreUnannotated
public class GoodsInStockVo {

    private static final long serialVersionUID = 1L;

    /**
     * 出库id
     */
    @ExcelProperty(value = "出库id")
    private Long id;

    /**
     * 仓库id
     */
    @ExcelProperty(value = "仓库id")
    private Long storageId;

    /**
     * 仓库名称
     */
    @ExcelProperty(value = "仓库名称")
    private String storageName;

    /**
     * 入库单号
     */
    @ExcelProperty(value = "入库单号")
    private String inStockNumbers;

    /**
     * 0:待入库;1:已入库；
     */
    @ExcelProperty(value = "0:待入库;1:已入库；")
    private Integer states;

    /**
     * 入库人
     */
    @ExcelProperty(value = "入库人")
    private String ingoingPerson;

    /**
     * 入库时间
     */
    @ExcelProperty(value = "入库时间")
    private Date ingoingTime;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    private String remarks;

    /**
     * 出库人
     */
    private String outgoingDay;

    private Date updateTime;

    private Date createTime;

    private String createBy;

    private List<InStockProductVo> inStockProductVoList;
}
