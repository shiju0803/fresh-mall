package com.zzq.storage.domain.bo;

import com.zzq.common.core.domain.BaseEntity;
import com.zzq.common.core.validate.EditGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * 仓库管理业务对象 kx_storage
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class StorageBo extends BaseEntity {

    /**
     * 仓库主键ID
     */
    @NotNull(message = "仓库主键ID不能为空", groups = { EditGroup.class })
    private Long id;

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 省
     */
    private Long province;

    /**
     * 市
     */
    private Long city;

    /**
     * 区（县）
     */
    private Long county;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 区域编码
     */
    private String adcode;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 0.禁用 1.正常
     */
    private Integer state;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 仓库管理电话
     */
    private String phone;

    /**
     * 仓库管理名称
     */
    private String leaderName;

    /**
     * 营业状态 0.休息 1.营业
     */
    private Integer operatingState;

    /**
     * 营业起始时间
     */
    private String businessStartTime;

    /**
     * 配送起始时间
     */
    private String deliveryStartTime;

    /**
     * 营业结束时间
     */
    private String businessStopTime;

    /**
     * 配送结束时间
     */
    private String deliveryStopTime;

    /**
     * 配送范围
     */
    private Long deliveryRadius;

    /**
     * 是否自动分配订单【0：非自动 1：自动】
     */
    private Integer automatic;

    /**
     * 状态 0.禁用 1.正常
     */
    private Integer printSwitch;

    /**
     * 账号名
     */
    private String printAcount;

    /**
     * Ukey
     */
    private String printUkey;

    /**
     * SN
     */
    private String printSn;

    /**
     * 公众号openId
     */
    private String openId;

    /**
     * 仓库权限参数
     */
    private Set<Long> storageIds;


    /**
     * 定位范围
     */
    private List<List<PointBo>> paths;

}
