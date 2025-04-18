package com.zzq.address.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;
import javax.validation.constraints.*;

import java.math.BigDecimal;

/**
 * 用户下单地址业务对象 kx_address
 * @date 2023-04-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AddressBo extends BaseEntity {

    /**
     *
     */
    @NotNull(message = "不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String county;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 默认地址
     */
    private Integer defaultAddress;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 收货人
     */
    private String consignee;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;


}
