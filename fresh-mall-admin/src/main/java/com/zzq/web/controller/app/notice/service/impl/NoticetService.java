package com.zzq.web.controller.app.notice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzq.system.domain.SysNotice;
import com.zzq.system.mapper.SysNoticeMapper;
import com.zzq.web.controller.app.notice.service.INoticetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class NoticetService implements INoticetService {

    private final SysNoticeMapper baseMapper;

    @Override
    public List<SysNotice> queryList(SysNotice bo) {
        return baseMapper.selectList(new LambdaQueryWrapper<SysNotice>().eq(SysNotice::getStatus, "0"));
    }
}
