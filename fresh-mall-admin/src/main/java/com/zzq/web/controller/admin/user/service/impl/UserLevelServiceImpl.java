package com.zzq.web.controller.admin.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.user.domain.UserLevel;
import com.zzq.user.domain.bo.UserLevelBo;
import com.zzq.user.domain.vo.UserLevelVo;
import com.zzq.user.mapper.UserLevelMapper;
import com.zzq.web.controller.admin.user.service.IUserLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 用户等级Service业务层处理
 * @date 2023-02-14
 */
@RequiredArgsConstructor
@Service
public class UserLevelServiceImpl implements IUserLevelService {

    private final UserLevelMapper baseMapper;

    /**
     * 查询用户等级
     */
    @Override
    public UserLevelVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户等级列表
     */
    @Override
    public TableDataInfo<UserLevelVo> queryPageList(UserLevelBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserLevel> lqw = buildQueryWrapper(bo);
        Page<UserLevelVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户等级列表
     */
    @Override
    public List<UserLevelVo> queryList(UserLevelBo bo) {
        LambdaQueryWrapper<UserLevel> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserLevel> buildQueryWrapper(UserLevelBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserLevel> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMerId() != null, UserLevel::getMerId, bo.getMerId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), UserLevel::getName, bo.getName());
        lqw.eq(bo.getMoney() != null, UserLevel::getMoney, bo.getMoney());
        lqw.eq(bo.getValidDate() != null, UserLevel::getValidDate, bo.getValidDate());
        lqw.eq(bo.getIsForever() != null, UserLevel::getIsForever, bo.getIsForever());
        lqw.eq(bo.getIsPay() != null, UserLevel::getIsPay, bo.getIsPay());
        lqw.eq(bo.getIsShow() != null, UserLevel::getIsShow, bo.getIsShow());
        lqw.eq(bo.getGrade() != null, UserLevel::getGrade, bo.getGrade());
        lqw.eq(bo.getDiscount() != null, UserLevel::getDiscount, bo.getDiscount());
        lqw.eq(StringUtils.isNotBlank(bo.getImage()), UserLevel::getImage, bo.getImage());
        lqw.eq(StringUtils.isNotBlank(bo.getIcon()), UserLevel::getIcon, bo.getIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getExplain()), UserLevel::getExplain, bo.getExplain());
        lqw.eq(bo.getIsDel() != null, UserLevel::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增用户等级
     */
    @Override
    public Boolean insertByBo(UserLevelBo bo) {
        UserLevel add = BeanUtil.toBean(bo, UserLevel.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改用户等级
     */
    @Override
    public Boolean updateByBo(UserLevelBo bo) {
        UserLevel update = BeanUtil.toBean(bo, UserLevel.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(UserLevel entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户等级
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
