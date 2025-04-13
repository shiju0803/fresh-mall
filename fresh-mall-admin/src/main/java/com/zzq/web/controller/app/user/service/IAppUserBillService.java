package com.zzq.web.controller.app.user.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserBillBo;
import com.zzq.user.domain.vo.UserBillVo;

/**
 * 用户账单Service接口
 * @date 2023-02-14
 */
public interface IAppUserBillService {



    /**
     * 查询用户账单列表
     */
    TableDataInfo<UserBillVo> queryPageList(UserBillBo bo, PageQuery pageQuery);


}
