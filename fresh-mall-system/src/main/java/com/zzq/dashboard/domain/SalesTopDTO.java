package com.zzq.dashboard.domain;

import lombok.Data;

@Data
public class SalesTopDTO {

    /**
     * 名称
     */
    private String title;
    /**
     * 商品ID
     */
    private Long productId;
    /**
     * 商品规格ID
     */
    private Long productAttrId;
    /**
     * 销量
     */
    private Long totalSales;
    /**
     * 销售额
     */
    private Float totalSalesVolume;


}
