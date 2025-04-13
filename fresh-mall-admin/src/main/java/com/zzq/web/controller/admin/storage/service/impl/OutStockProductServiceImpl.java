package com.zzq.web.controller.admin.storage.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.storage.domain.OutStockProduct;
import com.zzq.storage.domain.bo.OutStockProductBo;
import com.zzq.storage.domain.vo.OutStockProductVo;
import com.zzq.storage.mapper.OutStockProductMapper;
import com.zzq.web.controller.admin.storage.service.IOutStockProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 出库商品Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class OutStockProductServiceImpl implements IOutStockProductService {

    private final OutStockProductMapper baseMapper;

    /**
     * 查询出库商品
     */
    @Override
    public OutStockProductVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询出库商品列表
     */
    @Override
    public TableDataInfo<OutStockProductVo> queryPageList(OutStockProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OutStockProduct> lqw = buildQueryWrapper(bo);
        Page<OutStockProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询出库商品列表
     */
    @Override
    public List<OutStockProductVo> queryList(OutStockProductBo bo) {
        LambdaQueryWrapper<OutStockProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<OutStockProduct> buildQueryWrapper(OutStockProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<OutStockProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCategoryName()), OutStockProduct::getCategoryName, bo.getCategoryName());
        lqw.eq(StringUtils.isNotBlank(bo.getBarCode()), OutStockProduct::getBarCode, bo.getBarCode());
        lqw.eq(StringUtils.isNotBlank(bo.getOutStockNumbers()), OutStockProduct::getOutStockNumbers, bo.getOutStockNumbers());
        lqw.like(StringUtils.isNotBlank(bo.getProductName()), OutStockProduct::getProductName, bo.getProductName());
        lqw.like(StringUtils.isNotBlank(bo.getProductAttrName()), OutStockProduct::getProductAttrName, bo.getProductAttrName());
        lqw.eq(bo.getStock() != null, OutStockProduct::getStock, bo.getStock());
        lqw.eq(bo.getOutStockNum() != null, OutStockProduct::getOutStockNum, bo.getOutStockNum());
        lqw.eq(bo.getProductAttrId() != null, OutStockProduct::getProductAttrId, bo.getProductAttrId());
        return lqw;
    }

    /**
     * 新增出库商品
     */
    @Override
    public Boolean insertByBo(OutStockProductBo bo) {
        OutStockProduct add = BeanUtil.toBean(bo, OutStockProduct.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改出库商品
     */
    @Override
    public Boolean updateByBo(OutStockProductBo bo) {
        OutStockProduct update = BeanUtil.toBean(bo, OutStockProduct.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(OutStockProduct entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除出库商品
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }
}
