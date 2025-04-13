package com.zzq.web.controller.app.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.zzq.user.domain.User;
import com.zzq.user.domain.bo.UserBo;
import com.zzq.user.domain.vo.UserVo;
import com.zzq.user.mapper.UserMapper;
import com.zzq.web.controller.app.user.service.IAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 足迹Service业务层处理
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class AppUserService implements IAppUserService {

    private final UserMapper baseMapper;

    private static final String VERIFY_CODE_PREFIX = "VERIFY_CODE_";

    /**
     * 查询足迹
     */
    @Override
    public UserVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 修改足迹
     */
    @Override
    public Boolean updateByBo(UserBo bo) {
        User update = BeanUtil.toBean(bo, User.class);
        return baseMapper.updateById(update) > 0;
    }
}
