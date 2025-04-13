package com.zzq.web.controller.admin.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.product.domain.StoreProductAttrResult;
import com.zzq.product.domain.bo.StoreProductAttrResultBo;
import com.zzq.product.domain.vo.StoreProductAttrResultVo;
import com.zzq.product.mapper.StoreProductAttrResultMapper;
import com.zzq.web.controller.admin.product.service.IStoreProductAttrResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品属性详情Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreProductAttrResultServiceImpl implements IStoreProductAttrResultService {

    private final StoreProductAttrResultMapper baseMapper;

    /**
     * 查询商品属性详情
     */
    @Override
    public StoreProductAttrResultVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品属性详情列表
     */
    @Override
    public TableDataInfo<StoreProductAttrResultVo> queryPageList(StoreProductAttrResultBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreProductAttrResult> lqw = buildQueryWrapper(bo);
        Page<StoreProductAttrResultVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品属性详情列表
     */
    @Override
    public List<StoreProductAttrResultVo> queryList(StoreProductAttrResultBo bo) {
        LambdaQueryWrapper<StoreProductAttrResult> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreProductAttrResult> buildQueryWrapper(StoreProductAttrResultBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreProductAttrResult> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, StoreProductAttrResult::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getResult()), StoreProductAttrResult::getResult, bo.getResult());
        lqw.eq(bo.getChangeTime() != null, StoreProductAttrResult::getChangeTime, bo.getChangeTime());
        return lqw;
    }

    /**
     * 新增商品属性详情
     */
    @Override
    public Boolean insertByBo(StoreProductAttrResultBo bo) {
        StoreProductAttrResult add = BeanUtil.toBean(bo, StoreProductAttrResult.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品属性详情
     */
    @Override
    public Boolean updateByBo(StoreProductAttrResultBo bo) {
        StoreProductAttrResult update = BeanUtil.toBean(bo, StoreProductAttrResult.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreProductAttrResult entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品属性详情
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
