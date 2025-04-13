package com.zzq.web.controller.admin.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.user.domain.UserLevelSetting;
import com.zzq.user.domain.bo.UserLevelSettingBo;
import com.zzq.user.domain.vo.UserLevelSettingVo;
import com.zzq.user.mapper.UserLevelSettingMapper;
import com.zzq.web.controller.admin.user.service.IUserLevelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 设置用户等级Service业务层处理
 * @date 2023-02-21
 */
@RequiredArgsConstructor
@Service
public class UserLevelSettingServiceImpl implements IUserLevelSettingService {

    private final UserLevelSettingMapper baseMapper;

    /**
     * 查询设置用户等级
     */
    @Override
    public UserLevelSettingVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询设置用户等级列表
     */
    @Override
    public TableDataInfo<UserLevelSettingVo> queryPageList(UserLevelSettingBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserLevelSetting> lqw = buildQueryWrapper(bo);
        Page<UserLevelSettingVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询设置用户等级列表
     */
    @Override
    public List<UserLevelSettingVo> queryList(UserLevelSettingBo bo) {
        LambdaQueryWrapper<UserLevelSetting> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserLevelSetting> buildQueryWrapper(UserLevelSettingBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserLevelSetting> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getMerId() != null, UserLevelSetting::getMerId, bo.getMerId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), UserLevelSetting::getName, bo.getName());
        lqw.eq(bo.getMoney() != null, UserLevelSetting::getMoney, bo.getMoney());
        lqw.eq(bo.getValidDate() != null, UserLevelSetting::getValidDate, bo.getValidDate());
        lqw.eq(bo.getIsForever() != null, UserLevelSetting::getIsForever, bo.getIsForever());
        lqw.eq(bo.getIsPay() != null, UserLevelSetting::getIsPay, bo.getIsPay());
        lqw.eq(bo.getIsShow() != null, UserLevelSetting::getIsShow, bo.getIsShow());
        lqw.eq(bo.getGrade() != null, UserLevelSetting::getGrade, bo.getGrade());
        lqw.eq(bo.getDiscount() != null, UserLevelSetting::getDiscount, bo.getDiscount());
        lqw.eq(CollectionUtils.isNotEmpty(bo.getImage()), UserLevelSetting::getImage, bo.getImage());
        lqw.eq(CollectionUtils.isNotEmpty(bo.getIcon()), UserLevelSetting::getIcon, bo.getIcon());
        lqw.eq(StringUtils.isNotBlank(bo.getExplain()), UserLevelSetting::getExplain, bo.getExplain());
        lqw.eq(bo.getIsDel() != null, UserLevelSetting::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增设置用户等级
     */
    @Override
    public Boolean insertByBo(UserLevelSettingBo bo) {
        UserLevelSetting add = BeanUtil.toBean(bo, UserLevelSetting.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改设置用户等级
     */
    @Override
    public Boolean updateByBo(UserLevelSettingBo bo) {
        UserLevelSetting update = BeanUtil.toBean(bo, UserLevelSetting.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(UserLevelSetting entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除设置用户等级
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
