package com.zzq.web.controller.admin.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.product.domain.StoreProductAttrValue;
import com.zzq.product.domain.bo.StoreProductAttrValueBo;
import com.zzq.product.domain.vo.StoreProductAttrValueVo;
import com.zzq.product.mapper.StoreProductAttrValueMapper;
import com.zzq.web.controller.admin.product.service.IStoreProductAttrValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品属性值Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreProductAttrValueServiceImpl implements IStoreProductAttrValueService {

    private final StoreProductAttrValueMapper baseMapper;

    /**
     * 查询商品属性值
     */
    @Override
    public StoreProductAttrValueVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品属性值列表
     */
    @Override
    public TableDataInfo<StoreProductAttrValueVo> queryPageList(StoreProductAttrValueBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreProductAttrValue> lqw = buildQueryWrapper(bo);
        Page<StoreProductAttrValueVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品属性值列表
     */
    @Override
    public List<StoreProductAttrValueVo> queryList(StoreProductAttrValueBo bo) {
        LambdaQueryWrapper<StoreProductAttrValue> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreProductAttrValue> buildQueryWrapper(StoreProductAttrValueBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreProductAttrValue> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, StoreProductAttrValue::getProductId, bo.getProductId());
        lqw.eq(StringUtils.isNotBlank(bo.getSku()), StoreProductAttrValue::getSku, bo.getSku());
        lqw.eq(bo.getStock() != null, StoreProductAttrValue::getStock, bo.getStock());
        lqw.eq(bo.getSales() != null, StoreProductAttrValue::getSales, bo.getSales());
        lqw.eq(bo.getPrice() != null, StoreProductAttrValue::getPrice, bo.getPrice());
        lqw.eq(StringUtils.isNotBlank(bo.getImage()), StoreProductAttrValue::getImage, bo.getImage());
        lqw.eq(StringUtils.isNotBlank(bo.getUnique()), StoreProductAttrValue::getUnique, bo.getUnique());
        lqw.eq(bo.getCost() != null, StoreProductAttrValue::getCost, bo.getCost());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), StoreProductAttrValue::getBarCode, bo.getBarCode());
        lqw.eq(bo.getOtPrice() != null, StoreProductAttrValue::getOtPrice, bo.getOtPrice());
        lqw.eq(bo.getWeight() != null, StoreProductAttrValue::getWeight, bo.getWeight());
        lqw.eq(bo.getVolume() != null, StoreProductAttrValue::getVolume, bo.getVolume());
        lqw.eq(bo.getBrokerage() != null, StoreProductAttrValue::getBrokerage, bo.getBrokerage());
        lqw.eq(bo.getBrokerageTwo() != null, StoreProductAttrValue::getBrokerageTwo, bo.getBrokerageTwo());
        lqw.eq(bo.getPinkPrice() != null, StoreProductAttrValue::getPinkPrice, bo.getPinkPrice());
        lqw.eq(bo.getPinkStock() != null, StoreProductAttrValue::getPinkStock, bo.getPinkStock());
        lqw.eq(bo.getSeckillPrice() != null, StoreProductAttrValue::getSeckillPrice, bo.getSeckillPrice());
        lqw.eq(bo.getSeckillStock() != null, StoreProductAttrValue::getSeckillStock, bo.getSeckillStock());
        lqw.eq(bo.getIntegral() != null, StoreProductAttrValue::getIntegral, bo.getIntegral());
        return lqw;
    }

    /**
     * 新增商品属性值
     */
    @Override
    public Boolean insertByBo(StoreProductAttrValueBo bo) {
        StoreProductAttrValue add = BeanUtil.toBean(bo, StoreProductAttrValue.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品属性值
     */
    @Override
    public Boolean updateByBo(StoreProductAttrValueBo bo) {
        StoreProductAttrValue update = BeanUtil.toBean(bo, StoreProductAttrValue.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreProductAttrValue entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品属性值
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
