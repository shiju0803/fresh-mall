package com.zzq.system.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 快递公司业务对象 sys_express
 * @date 2023-02-17
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class SysExpressBo extends BaseEntity {

    /**
     * 快递公司id
     */
    private Long id;

    /**
     * 快递公司简称
     */
    private String code;

    /**
     * 快递公司全称
     */
    private String name;

    /**
     * 排序
     */
    private Long sort;

    /**
     * 是否显示
     */
    private Integer isShow;

    /**
     *
     */
    private Long isDel;


}
