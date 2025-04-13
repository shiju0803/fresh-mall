package com.zzq.web.controller.app.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.OrderStatusType;
import com.zzq.common.exception.ServiceException;
import com.zzq.order.domain.StoreAppraise;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.StoreOrderProduct;
import com.zzq.order.domain.bo.AppraiseRequestBo;
import com.zzq.order.domain.bo.StoreAppraiseBo;
import com.zzq.order.domain.vo.StoreAppraiseVo;
import com.zzq.order.mapper.StoreAppraiseMapper;
import com.zzq.order.mapper.StoreOrderMapper;
import com.zzq.order.mapper.StoreOrderProductMapper;
import com.zzq.web.controller.app.order.service.IAppAppraiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AppAppraiseService implements IAppAppraiseService {


    private final StoreOrderMapper orderMapper;

    private final StoreOrderProductMapper orderProductMapper;

    private final StoreAppraiseMapper baseMapper;


    @Override
    public TableDataInfo<StoreAppraiseVo> getProductAppraiseByPage(Long productId, Integer pageNo, Integer pageSize, Integer state) {
//        String cacheKey = CA_APPRAISE_KEY + productId + "_" + pageNo + "_" + pageSize;
//        TableDataInfo<StoreAppraiseVo> obj = RedisUtils.getCacheObject(cacheKey);
//        if (obj != null) {
//            return obj;
//        }
        Long count = baseMapper.selectCount(new QueryWrapper<StoreAppraise>().eq("product_id", productId).eq("state", state));
        Integer offset = pageSize * (pageNo - 1);
        List<StoreAppraiseVo> storeAppraiseVoList = baseMapper.selectProductAppraiseByPage(productId, offset, pageSize);
//        for (StoreAppraiseVo appraiseResponseDTO : storeAppraiseVoList) {
//            appraiseResponseDTO.setImgList(imgMapper.getImgs(BizType.COMMENT.getCode(), appraiseResponseDTO.getId()));
//        }

        TableDataInfo<StoreAppraiseVo> info = new TableDataInfo<>(storeAppraiseVoList, count);
//        RedisUtils.setCacheObject(cacheKey, info);
        return info;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addAppraise(AppraiseRequestBo bo, Long userId) throws ServiceException {
        if (bo.getOrderId() == null) {
            throw new ServiceException("参数校验失败");
        }
        //校验是否有对应等待评价的订单
        Long integer = orderMapper.selectCount(
                new QueryWrapper<StoreOrder>()
                        .eq("id", bo.getOrderId())
                        .eq("status", OrderStatusType.WAIT_APPRAISE.getCode())
                        .eq("uid", userId));
        if (integer == 0L) {
            throw new ServiceException("当前状态不允许评价");
        }

        //如果传入评价list中没有数据，就直接转变订单状态发出
        Date now = new Date();
        if (CollectionUtils.isEmpty(bo.getAppraiseDTOList())) {
            StoreOrder orderDO = StoreOrder.builder().build();
            orderDO.setStatus(OrderStatusType.COMPLETE.getCode());
            orderDO.setId(bo.getOrderId());
            orderDO.setUpdateTime(now);
            orderMapper.updateById(orderDO);
        }

        //循环读取订单评价中所有商品的评价
        for (StoreAppraiseBo appraiseDTO : bo.getAppraiseDTOList()) {
            Long count = orderProductMapper.selectCount(new QueryWrapper<StoreOrderProduct>()
                    .eq("order_id", bo.getOrderId())
                    .eq("product_id", appraiseDTO.getProductId())
                    .eq("product_attr_id", appraiseDTO.getProductAttrId()));
            //从order_sku表中 验证是否有对应的表单和商品
            if (count == 0L) {
                throw new ServiceException("当前状态不允许评价");
            }

            StoreAppraise appraiseDO = new StoreAppraise();
            BeanUtils.copyProperties(appraiseDTO, appraiseDO);
            appraiseDO.setProductId(appraiseDTO.getProductId());
            appraiseDO.setId(null); //防止传入id,导致插入数据库出错
            appraiseDO.setOrderId(bo.getOrderId()); //从传入数据取出，不使用DTO中的冗余数据
            appraiseDO.setUserId(userId);
            appraiseDO.setUpdateTime(now);
            appraiseDO.setCreateTime(now);
            // TODO 预留
            //appraiseDO.setStorageId(bo.getStorageId());
            appraiseDO.setContent(appraiseDTO.getContent());
            baseMapper.insert(appraiseDO);  //插入该订单该商品评价
//            RedisUtils.deleteKeys(CA_APPRAISE_KEY + appraiseDO.getProductId()+"*"); //删除商品评论缓存

        }
        //改变订单状态
        StoreOrder orderDO = StoreOrder.builder().build();
        orderDO.setStatus(OrderStatusType.COMPLETE.getCode());
        orderDO.setId(bo.getOrderId());
        orderDO.setUpdateTime(now);
        orderMapper.updateById(orderDO);
        return true;
    }
}
