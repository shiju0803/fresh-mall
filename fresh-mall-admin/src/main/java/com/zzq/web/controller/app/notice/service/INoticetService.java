package com.zzq.web.controller.app.notice.service;

import com.zzq.system.domain.SysNotice;

import java.util.List;

/**
 * 公告Service接口
 * @date 2023-04-06
 */
public interface INoticetService {


    /**
     * 查询公告
     */
    List<SysNotice> queryList(SysNotice bo);

}
