package com.zzq.region.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;


/**
 * 中国地区信息对象 kx_region
 */
@Data
@TableName("kx_region")
public class Region {

    private static final long serialVersionUID=1L;

    /**
     * 自增ID
     */
    @TableId(value = "id")
    private Long id;
    /**
     * 代码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 简称
     */
    private String shortName;
    /**
     * 上级代码
     */
    private String superiorCode;
    /**
     * 经度
     */
    private String lng;
    /**
     * 纬度
     */
    private String lat;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String ramark;
    /**
     * 状态
     */
    private Long state;
    /**
     * 租户ID
     */
    private String tenantCode;
    /**
     * 级别
     */
    private Long level;




    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;



    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
}
