package com.zzq.web.controller.app.user.service;

import com.zzq.user.domain.bo.UserBo;
import com.zzq.user.domain.vo.UserVo;

/**
 * 足迹Service接口
 * @date 2023-04-06
 */
public interface IAppUserService {

    /**
     * 查询用户
     */
    UserVo queryById(Long id);


    /**
     * 修改用户
     */
    Boolean updateByBo(UserBo bo);

}
