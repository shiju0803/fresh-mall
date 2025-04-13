package com.zzq.web.controller.admin.activity.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.activity.domain.StoreActivityProduct;
import com.zzq.activity.domain.bo.StoreActivityProductBo;
import com.zzq.activity.domain.vo.StoreActivityProductVo;
import com.zzq.activity.mapper.StoreActivityProductMapper;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.exception.ServiceException;
import com.zzq.product.domain.StoreProduct;
import com.zzq.product.mapper.StoreProductMapper;
import com.zzq.web.controller.admin.activity.service.IStoreActivityProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 活动商品Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreActivityProductServiceImpl implements IStoreActivityProductService {

    private final StoreActivityProductMapper baseMapper;

    private final StoreProductMapper kxStoreProductMapper;


    /**
     * 查询活动商品
     */
    @Override
    public StoreActivityProductVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询活动商品列表
     */
    @Override
    public TableDataInfo<StoreActivityProductVo> queryPageList(StoreActivityProductBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreActivityProduct> lqw = buildQueryWrapper(bo);
        Page<StoreActivityProductVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询活动商品列表
     */
    @Override
    public List<StoreActivityProductVo> queryList(StoreActivityProductBo bo) {
        LambdaQueryWrapper<StoreActivityProduct> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreActivityProduct> buildQueryWrapper(StoreActivityProductBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreActivityProduct> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, StoreActivityProduct::getProductId, bo.getProductId());
        lqw.eq(bo.getActivityId() != null, StoreActivityProduct::getActivityId, bo.getActivityId());
        return lqw;
    }

    /**
     * 新增活动商品
     */
    @Override
    public Boolean insertByBo(StoreActivityProductBo bo) {
        StoreActivityProduct add = BeanUtil.toBean(bo, StoreActivityProduct.class);
        validEntityBeforeSave(add);

        //获取当前商品价格
        StoreProduct kxStoreProduct = kxStoreProductMapper.selectById(bo.getProductId());
        bo.setActivityPrice(kxStoreProduct.getPrice());

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
    public Boolean updateByBo(StoreActivityProductBo bo) {
        StoreActivityProduct update = BeanUtil.toBean(bo, StoreActivityProduct.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreActivityProduct entity){
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

    @Override
    public Boolean addProductBatch(StoreActivityProductBo bo) {
        if (CollectionUtils.isEmpty(bo.getProductIds())) {
            throw new ServiceException("你要加入活动的商品不存在");
        }

        baseMapper.delete(new LambdaQueryWrapper<StoreActivityProduct>()
                .in(StoreActivityProduct::getProductId, bo.getProductIds())
                .eq(StoreActivityProduct::getActivityId, bo.getActivityId()));

        List<StoreActivityProduct> productDOList = new ArrayList<>();
        for (Long productId : bo.getProductIds()) {
            StoreActivityProduct add = new StoreActivityProduct();
            add.setProductId(productId);

            //获取当前商品价格
            StoreProduct kxStoreProduct = kxStoreProductMapper.selectById(productId);
            add.setActivityPrice(kxStoreProduct.getPrice());

            add.setActivityId(bo.getActivityId());
            Date now = new Date();
            add.setUpdateTime(now);
            add.setCreateTime(now);
            productDOList.add(add);
        }
        baseMapper.insertBatch(productDOList);
//        RedisUtils.deleteKeys(RECOMMEND_NAME + "*");
        return true;
    }

    @Override
    public Boolean updatePrice(StoreActivityProductBo bo) {
        StoreActivityProduct update = new StoreActivityProduct();
        update.setUpdateTime(new Date());
        update.setActivityPrice(bo.getActivityPrice());
        update.setId(bo.getId());
        return baseMapper.update(update, new LambdaQueryWrapper<StoreActivityProduct>().eq(StoreActivityProduct::getId, bo.getId())) > 0;
    }
}
