package com.zzq.user.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zzq.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户对象 kx_user
 * @date 2023-02-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("kx_user")
public class User extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 用户id
     */
    @TableId(value = "uid")
    private Long uid;
    /**
     * 用户账户(跟accout一样)
     */
    private String username;
    /**
     * 用户密码（跟pwd）
     */
    private String password;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 生日
     */
    private Long birthday;
    /**
     * 身份证号码
     */
    private String cardId;
    /**
     * 用户备注
     */
    private String mark;
    /**
     * 合伙人id
     */
    private Long partnerId;
    /**
     * 用户分组id
     */
    private Long groupId;
    /**
     * 用户昵称
     */
    private String nickname;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 添加ip
     */
    private String addIp;
    /**
     * 最后一次登录ip
     */
    private String lastIp;
    /**
     * 用户余额
     */
    private BigDecimal nowMoney;
    /**
     * 佣金金额
     */
    private BigDecimal brokeragePrice;
    /**
     * 用户剩余积分
     */
    private BigDecimal integral;
    /**
     * 连续签到天数
     */
    private Long signNum;
    /**
     * 1为正常，0为禁止
     */
    private Integer status;
    /**
     * 等级
     */
    private Integer level;
    /**
     * 推广元id
     */
    private Long spreadUid;
    /**
     * 推广员关联时间
     */
    private Date spreadTime;
    /**
     * 用户类型
     */
    private String userType;
    /**
     * 是否为推广员
     */
    private Integer isPromoter;
    /**
     * 用户购买次数
     */
    private Long payCount;
    /**
     * 下级人数
     */
    private Long spreadCount;
    /**
     * 详细地址
     */
    private String addres;
    /**
     * 管理员编号
     */
    private Long adminid;
    /**
     * 用户登陆类型，h5,wechat,routine
     */
    private Integer loginType;
    /**
     * 微信用户json信息
     */
    private String wxProfile;
    /**
     *
     */
    private Integer isDel;

    /**
     * openId
     */
    private String openId;
    /**
     * 公众号openId
     */
    private String gzhOpenId;
    /**
     * unionId一个体系下唯一id
     */
    private String unionId;

}
