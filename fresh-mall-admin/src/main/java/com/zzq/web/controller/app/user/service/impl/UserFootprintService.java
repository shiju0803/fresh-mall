package com.zzq.web.controller.app.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.UserFootprint;
import com.zzq.user.domain.bo.UserFootprintBo;
import com.zzq.user.domain.vo.UserFootprintVo;
import com.zzq.user.mapper.UserFootprintMapper;
import com.zzq.web.controller.app.user.service.IUserFootprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 足迹Service业务层处理
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class UserFootprintService implements IUserFootprintService {

    private final UserFootprintMapper baseMapper;

    /**
     * 查询足迹
     */
    @Override
    public UserFootprintVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询足迹列表
     */
    @Override
    public TableDataInfo<UserFootprintVo> queryPageList(UserFootprintBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserFootprint> lqw = buildQueryWrapper(bo);
        Page<UserFootprintVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询足迹列表
     */
    @Override
    public List<UserFootprintVo> queryList(UserFootprintBo bo) {
        LambdaQueryWrapper<UserFootprint> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserFootprint> buildQueryWrapper(UserFootprintBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserFootprint> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, UserFootprint::getUserId, bo.getUserId());
        lqw.eq(bo.getProductId() != null, UserFootprint::getProductId, bo.getProductId());
        return lqw;
    }

    /**
     * 新增足迹
     */
    @Override
    public Boolean insertByBo(UserFootprintBo bo) {
        UserFootprint add = BeanUtil.toBean(bo, UserFootprint.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改足迹
     */
    @Override
    public Boolean updateByBo(UserFootprintBo bo) {
        UserFootprint update = BeanUtil.toBean(bo, UserFootprint.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(UserFootprint entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除足迹
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public boolean addOrUpdateFootprint(Long userId, Long producId) {
        Date now = new Date();
        List<UserFootprint> footprintDOList = baseMapper.selectList(
            new QueryWrapper<UserFootprint>()
                .eq("user_id", userId)
                .eq("product_id", producId)
                .orderByDesc("update_time"));
        if (CollectionUtils.isEmpty(footprintDOList)) {
            UserFootprint footprintDO = new UserFootprint();
            footprintDO.setUserId(userId);
            footprintDO.setProductId(producId);
            footprintDO.setUpdateTime(now);
            footprintDO.setCreateTime(now);
            return baseMapper.insert(footprintDO) > 0;
        }
        UserFootprint footprintDO = footprintDOList.get(0);
        footprintDO.setUpdateTime(now);
        return baseMapper.updateById(footprintDO) > 0;
    }
}
