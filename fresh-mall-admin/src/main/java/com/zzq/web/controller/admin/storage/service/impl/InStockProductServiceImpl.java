package com.zzq.web.controller.admin.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.storage.domain.InStockProduct;
import com.zzq.storage.domain.bo.InStockProductBo;
import com.zzq.storage.domain.vo.InStockProductVo;
import com.zzq.storage.mapper.InStockProductMapper;
import com.zzq.web.controller.admin.storage.service.IInStockProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 入库商品Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class InStockProductServiceImpl implements IInStockProductService {

    private final InStockProductMapper baseMapper;

    /**
     * 查询入库商品
     */
    @Override
    public InStockProductVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询入库商品列表
     */
    @Override
    public TableDataInfo<InStockProductVo> queryPageList(InStockProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<InStockProduct> lqw = buildQueryWrapper(bo);
        Page<InStockProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询入库商品列表
     */
    @Override
    public List<InStockProductVo> queryList(InStockProductBo bo) {
        LambdaQueryWrapper<InStockProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<InStockProduct> buildQueryWrapper(InStockProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<InStockProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryName()), InStockProduct::getCategoryName, bo.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), InStockProduct::getBarCode, bo.getBarCode());
        lqw.eq(StringUtils.isNotBlank(bo.getInStockNumbers()), InStockProduct::getInStockNumbers, bo.getInStockNumbers());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), InStockProduct::getProductName, bo.getProductName());
        lqw.like(StringUtils.isNotBlank(bo.getProductAttrName()), InStockProduct::getProductAttrName, bo.getProductAttrName());
        lqw.eq(bo.getStock() != null, InStockProduct::getStock, bo.getStock());
        lqw.eq(bo.getInStockNum() != null, InStockProduct::getInStockNum, bo.getInStockNum());
        lqw.eq(bo.getProductAttrId() != null, InStockProduct::getProductAttrId, bo.getProductAttrId());
        return lqw;
    }

    /**
     * 新增入库商品
     */
    @Override
    public Boolean insertByBo(InStockProductBo bo) {
        InStockProduct add = BeanUtil.toBean(bo, InStockProduct.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改入库商品
     */
    @Override
    public Boolean updateByBo(InStockProductBo bo) {
        InStockProduct update = BeanUtil.toBean(bo, InStockProduct.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(InStockProduct entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除入库商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
