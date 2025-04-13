package com.zzq.user.domain.vo;

import lombok.Data;


@Data
public class PromUserVo {
    private String avatar;
    private String  nickname;
    private Integer childCount;
    private Integer numberCount;
    private Integer  orderCount;
    private Integer uid;
    private String time;
}
