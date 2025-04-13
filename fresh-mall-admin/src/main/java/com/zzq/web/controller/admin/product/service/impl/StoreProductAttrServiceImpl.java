package com.zzq.web.controller.admin.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.StringUtils;
import com.zzq.product.domain.StoreProductAttr;
import com.zzq.product.domain.StoreProductAttrResult;
import com.zzq.product.domain.StoreProductAttrValue;
import com.zzq.product.domain.bo.StoreProductAttrBo;
import com.zzq.product.domain.vo.FromatDetailVo;
import com.zzq.product.domain.vo.ProductFormatVo;
import com.zzq.product.domain.vo.StoreProductAttrVo;
import com.zzq.product.mapper.StoreProductAttrMapper;
import com.zzq.product.mapper.StoreProductAttrResultMapper;
import com.zzq.product.mapper.StoreProductAttrValueMapper;
import com.zzq.web.controller.admin.product.service.IStoreProductAttrService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * 商品属性Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreProductAttrServiceImpl implements IStoreProductAttrService {

    private final StoreProductAttrMapper baseMapper;

    private final StoreProductAttrValueMapper storeProductAttrValueMapper;

    private final StoreProductAttrResultMapper storeProductAttrResultMapper;

    /**
     * 查询商品属性
     */
    @Override
    public StoreProductAttrVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品属性列表
     */
    @Override
    public TableDataInfo<StoreProductAttrVo> queryPageList(StoreProductAttrBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreProductAttr> lqw = buildQueryWrapper(bo);
        Page<StoreProductAttrVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品属性列表
     */
    @Override
    public List<StoreProductAttrVo> queryList(StoreProductAttrBo bo) {
        LambdaQueryWrapper<StoreProductAttr> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreProductAttr> buildQueryWrapper(StoreProductAttrBo bo) {
        LambdaQueryWrapper<StoreProductAttr> lqw = Wrappers.lambdaQuery();
        if (ObjectUtils.isEmpty(bo)) {
            return lqw;
        }
        Map<String, Object> params = bo.getParams();
        lqw.eq(bo.getProductId() != null, StoreProductAttr::getProductId, bo.getProductId());
        lqw.like(StringUtils.isNotBlank(bo.getAttrName()), StoreProductAttr::getAttrName, bo.getAttrName());
        lqw.eq(StringUtils.isNotBlank(bo.getAttrValues()), StoreProductAttr::getAttrValues, bo.getAttrValues());
        lqw.eq(bo.getIsDel() != null, StoreProductAttr::getIsDel, bo.getIsDel());
        return lqw;
    }

    /**
     * 新增商品属性
     */
    @Override
    public Boolean insertByBo(StoreProductAttrBo bo) {
        StoreProductAttr add = BeanUtil.toBean(bo, StoreProductAttr.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改商品属性
     */
    @Override
    public Boolean updateByBo(StoreProductAttrBo bo) {
        StoreProductAttr update = BeanUtil.toBean(bo, StoreProductAttr.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreProductAttr entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品属性
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public void insertYxStoreProductAttr(List<FromatDetailVo> items, List<ProductFormatVo> attrs, Long productId) {
        List<StoreProductAttr> attrGroup = new ArrayList<>();
        for (FromatDetailVo fromatDetailDto : items) {
            StoreProductAttr kxStoreProductAttr = StoreProductAttr.builder()
                .productId(productId)
                .attrName(fromatDetailDto.getValue())
                .attrValues(StrUtil.join(",",fromatDetailDto.getDetail()))
                .build();

            attrGroup.add(kxStoreProductAttr);
        }



        List<StoreProductAttrValue> valueGroup = new ArrayList<>();
        for (ProductFormatVo productFormatDto : attrs) {

            if(productFormatDto.getPinkStock()>productFormatDto.getStock() || productFormatDto.getSeckillStock()>productFormatDto.getStock()){
                throw new ServiceException("活动商品库存不能大于原有商品库存");
            }
            List<String> stringList = new ArrayList<>(productFormatDto.getDetail().values());
            Collections.sort(stringList);
            StoreProductAttrValue oldAttrValue = storeProductAttrValueMapper.selectOne(new LambdaQueryWrapper<StoreProductAttrValue>()
                .eq(StoreProductAttrValue::getSku, productFormatDto.getSku())
                .eq(StoreProductAttrValue::getProductId, productId));

            String unique = IdUtil.simpleUUID();
            if (Objects.nonNull(oldAttrValue)) {
                unique = oldAttrValue.getUnique();
            }

            StoreProductAttrValue yxStoreProductAttrValue = StoreProductAttrValue.builder()
                .id(Objects.isNull(oldAttrValue) ? null : oldAttrValue.getId())
                .productId(productId)
                .sku(StrUtil.join(",",stringList))
                .price(BigDecimal.valueOf(productFormatDto.getPrice()))
                .cost(BigDecimal.valueOf(productFormatDto.getCost()))
                .otPrice(BigDecimal.valueOf(productFormatDto.getOtPrice()))
                .unique(unique)
                .image(productFormatDto.getPic())
                .barCode(productFormatDto.getBarCode())
                .weight(BigDecimal.valueOf(productFormatDto.getWeight()))
                .volume(BigDecimal.valueOf(productFormatDto.getVolume()))
                .brokerage(BigDecimal.valueOf(productFormatDto.getBrokerage()))
                .brokerageTwo(BigDecimal.valueOf(productFormatDto.getBrokerageTwo()))
                .stock(productFormatDto.getStock())
                .integral(productFormatDto.getIntegral())
                .pinkPrice(BigDecimal.valueOf(productFormatDto.getPinkPrice()==null?0:productFormatDto.getPinkPrice()))
                .seckillPrice(BigDecimal.valueOf(productFormatDto.getSeckillPrice()==null?0:productFormatDto.getSeckillPrice()))
                .pinkStock(productFormatDto.getPinkStock()==null?0:productFormatDto.getPinkStock())
                .seckillStock(productFormatDto.getSeckillStock()==null?0:productFormatDto.getSeckillStock())
                .build();

            valueGroup.add(yxStoreProductAttrValue);
        }

        if(attrGroup.isEmpty() || valueGroup.isEmpty()){
            throw new ServiceException("请设置至少一个属性!");
        }

        //清理属性
        this.clearProductAttr(productId);

        //批量添加
        baseMapper.insertBatch(attrGroup);

        storeProductAttrValueMapper.insertBatch(valueGroup);


        Map<String,Object> map = new LinkedHashMap<>();
        map.put("attr",items);
        map.put("value",attrs);

        StoreProductAttrResult kxStoreProductAttrResult = new StoreProductAttrResult();
        kxStoreProductAttrResult.setProductId(productId);
        kxStoreProductAttrResult.setResult(JSON.toJSONString(map));
        kxStoreProductAttrResult.setChangeTime(new Date());

        long count = storeProductAttrResultMapper.selectCount(new LambdaQueryWrapper<StoreProductAttrResult>()
            .eq(StoreProductAttrResult::getProductId,productId));
        if(count > 0) {
            storeProductAttrResultMapper.delete(new LambdaQueryWrapper<StoreProductAttrResult>()
                .eq(StoreProductAttrResult::getProductId,productId));
        }
        storeProductAttrResultMapper.insertOrUpdate(kxStoreProductAttrResult);
    }

    @Override
    public List<StoreProductAttr> queryListAll() {
        return baseMapper.selectList();
    }

    /**
     * 删除YxStoreProductAttrValue表的属性
     * @param productId 商品id
     */
    private void clearProductAttr(Long productId) {
        if(ObjectUtil.isNull(productId)) {
            throw new ServiceException("产品不存在");
        }

        baseMapper.delete(Wrappers.<StoreProductAttr>lambdaQuery()
            .eq(StoreProductAttr::getProductId,productId));
        storeProductAttrValueMapper.delete(Wrappers.<StoreProductAttrValue>lambdaQuery()
            .eq(StoreProductAttrValue::getProductId,productId));

    }

}
