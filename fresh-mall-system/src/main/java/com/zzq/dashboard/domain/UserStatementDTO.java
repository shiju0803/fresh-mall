package com.zzq.dashboard.domain;

import lombok.Data;

@Data
public class UserStatementDTO {

    private Long id;

    /**
     * 统计日期
     */
    private String statementDate;
    /**
     * 总用户数
     */
    private Integer totalUser;
    /**
     * 新注册用户数
     */
    private Integer newUser;
    /**
     * 在线用户数
     */
    private Integer OnlineUser;
    /**
     * 下单用户数
     */
    private Integer orderUser;
    /**
     * 首单用户数
     */
    private Integer firstOrderUser;

}
