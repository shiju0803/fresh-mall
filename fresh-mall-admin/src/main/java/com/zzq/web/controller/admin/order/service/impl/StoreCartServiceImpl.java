package com.zzq.web.controller.admin.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.order.domain.StoreCart;
import com.zzq.order.domain.bo.StoreCartBo;
import com.zzq.order.domain.vo.StoreCartVo;
import com.zzq.order.mapper.StoreCartMapper;
import com.zzq.web.controller.admin.order.service.IStoreCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 购物车Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreCartServiceImpl implements IStoreCartService {

    private final StoreCartMapper baseMapper;

    /**
     * 查询购物车
     */
    @Override
    public StoreCartVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询购物车列表
     */
    @Override
    public TableDataInfo<StoreCartVo> queryPageList(StoreCartBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreCart> lqw = buildQueryWrapper(bo);
        Page<StoreCartVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询购物车列表
     */
    @Override
    public List<StoreCartVo> queryList(StoreCartBo bo) {
        LambdaQueryWrapper<StoreCart> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreCart> buildQueryWrapper(StoreCartBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreCart> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, StoreCart::getProductId, bo.getProductId());
        return lqw;
    }

    /**
     * 新增购物车
     */
    @Override
    public Boolean insertByBo(StoreCartBo bo) {
        StoreCart add = BeanUtil.toBean(bo, StoreCart.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改购物车
     */
    @Override
    public Boolean updateByBo(StoreCartBo bo) {
        StoreCart update = BeanUtil.toBean(bo, StoreCart.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreCart entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除购物车
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
