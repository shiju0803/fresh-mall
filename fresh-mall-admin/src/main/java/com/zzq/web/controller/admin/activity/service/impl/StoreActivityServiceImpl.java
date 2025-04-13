package com.zzq.web.controller.admin.activity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.activity.domain.StoreActivity;
import com.zzq.activity.domain.bo.StoreActivityBo;
import com.zzq.activity.domain.vo.StoreActivityVo;
import com.zzq.activity.mapper.StoreActivityMapper;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.web.controller.admin.activity.service.IStoreActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 活动商品Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreActivityServiceImpl implements IStoreActivityService {

    private final StoreActivityMapper baseMapper;

    /**
     * 查询活动商品
     */
    @Override
    public StoreActivityVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询活动商品列表
     */
    @Override
    public TableDataInfo<StoreActivityVo> queryPageList(StoreActivityBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreActivity> lqw = buildQueryWrapper(bo);
        Page<StoreActivityVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动商品列表
     */
    @Override
    public List<StoreActivityVo> queryList(StoreActivityBo bo) {
        LambdaQueryWrapper<StoreActivity> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreActivity> buildQueryWrapper(StoreActivityBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getTitle()), StoreActivity::getTitle, bo.getTitle());
        lqw.eq(StringUtils.isNotBlank(bo.getImgUrl()), StoreActivity::getImgUrl, bo.getImgUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getOutUrl()), StoreActivity::getOutUrl, bo.getOutUrl());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), StoreActivity::getDescription, bo.getDescription());
        lqw.eq(bo.getStatus() != null, StoreActivity::getStatus, bo.getStatus());
        return lqw;
    }

    /**
     * 新增活动商品
     */
    @Override
    public Boolean insertByBo(StoreActivityBo bo) {
        StoreActivity add = BeanUtil.toBean(bo, StoreActivity.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改活动商品
     */
    @Override
    public Boolean updateByBo(StoreActivityBo bo) {
        StoreActivity update = BeanUtil.toBean(bo, StoreActivity.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreActivity entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除活动商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
