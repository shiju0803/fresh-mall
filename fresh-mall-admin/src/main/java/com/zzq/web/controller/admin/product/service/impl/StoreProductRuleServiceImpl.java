package com.zzq.web.controller.admin.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.product.domain.StoreProductRule;
import com.zzq.product.domain.bo.StoreProductRuleBo;
import com.zzq.product.domain.vo.StoreProductRuleVo;
import com.zzq.product.mapper.StoreProductRuleMapper;
import com.zzq.web.controller.admin.product.service.IStoreProductRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品规格Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreProductRuleServiceImpl implements IStoreProductRuleService {

    private final StoreProductRuleMapper baseMapper;

    /**
     * 查询商品规格
     */
    @Override
    public StoreProductRuleVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品规格列表
     */
    @Override
    public TableDataInfo<StoreProductRuleVo> queryPageList(StoreProductRuleBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreProductRule> lqw = buildQueryWrapper(bo);
        Page<StoreProductRuleVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品规格列表
     */
    @Override
    public List<StoreProductRuleVo> queryList(StoreProductRuleBo bo) {
        LambdaQueryWrapper<StoreProductRule> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreProductRule> buildQueryWrapper(StoreProductRuleBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreProductRule> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotBlank(bo.getRuleName()), StoreProductRule::getRuleName, bo.getRuleName());
        return lqw;
    }

    /**
     * 新增商品规格
     */
    @Override
    public Boolean insertByBo(StoreProductRuleBo bo) {
        StoreProductRule add = BeanUtil.toBean(bo, StoreProductRule.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品规格
     */
    @Override
    public Boolean updateByBo(StoreProductRuleBo bo) {
        StoreProductRule update = BeanUtil.toBean(bo, StoreProductRule.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreProductRule entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品规格
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<StoreProductRuleVo> queryListAll() {
      return baseMapper.selectVoList(null);
    }
}
