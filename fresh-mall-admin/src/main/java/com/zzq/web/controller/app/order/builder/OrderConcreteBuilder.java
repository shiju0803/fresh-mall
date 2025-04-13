package com.zzq.web.controller.app.order.builder;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzq.address.domain.Address;
import com.zzq.address.mapper.AddressMapper;
import com.zzq.common.enums.OrderStatusType;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.GeneratorUtil;
import com.zzq.order.domain.StoreCart;
import com.zzq.order.domain.StoreOrder;
import com.zzq.order.domain.StoreOrderProduct;
import com.zzq.order.domain.bo.OrderPriceBo;
import com.zzq.order.domain.bo.OrderRequestBo;
import com.zzq.order.domain.bo.OrderRequestProductBo;
import com.zzq.order.mapper.StoreCartMapper;
import com.zzq.order.mapper.StoreOrderMapper;
import com.zzq.order.mapper.StoreOrderProductMapper;
import com.zzq.orderobserve.OrderObserveAble;
import com.zzq.orderobserve.OrderUpdater;
import com.zzq.product.domain.vo.StoreProductVo;
import com.zzq.product.mapper.StoreProductMapper;
import com.zzq.web.controller.app.product.service.IAppCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 订单具体建造者
 **/
@Component
@SuppressWarnings("all")
public class OrderConcreteBuilder extends OrderBuilder {

    private static final Logger logger = LoggerFactory.getLogger(OrderConcreteBuilder.class);

    @Autowired
    private StoreProductMapper productMapper;

    @Autowired
    private StoreCartMapper cartMapper;

    @Autowired
    private StoreOrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Resource
    private OrderObserveAble observeAble;

    @Autowired
    private StoreOrderProductMapper orderProductMapper;


    @Autowired
    private IAppCategoryService categoryService;


    /**
     * 1.订单初始创建校验部分
     *
     * @param orderRequest
     * @param userId
     */
    @Override
    public void buildOrderCheckHandlePart(OrderRequestBo orderRequest, Long userId) throws ServiceException {
        //参数强校验 START
        List<OrderRequestProductBo> skuList = orderRequest.getProductList();
        if (CollectionUtils.isEmpty(skuList) || orderRequest.getTotalPrice() == null) {
            throw new ServiceException("参数校验失败");
        }
        if (orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("订单金额必须大于0");
        }
    }

    /**
     * 2.订单价格处理部分
     *
     * @param orderDO
     * @param orderRequest
     * @param userId
     */
    @Override
    public OrderPriceBo buildOrderPriceHandlePart(StoreOrder orderDO, OrderRequestBo orderRequest, Long userId) throws ServiceException {
        List<OrderRequestProductBo> skuList = orderRequest.getProductList();
        Long groupShopId = orderRequest.getGroupShopId();
        BigDecimal groupShopPrice = null;
        //商品价格
        BigDecimal productPrice = BigDecimal.ZERO;
        BigDecimal productOriginalPrice = BigDecimal.ZERO;
        //稍后用于优惠券作用范围校验
        Map<Long, BigDecimal> categoryPriceMap = new HashMap<>();
        //稍后用于插入StoreProductVo
        StoreProductVo product;
        Map<Long, StoreProductVo> productIdDTOMap = new HashMap<>();
        for (OrderRequestProductBo orderRequestStoreProduct : skuList) {
            //每个地方仓库的价格不一样，所以需要查询各自商户设置的价格
            product = productMapper.getProductByIdAndStorageId(orderRequestStoreProduct.getProductId(), orderRequest.getStorageId());
            if (product == null) {
                throw new ServiceException("商品并不存在");
            }
            productIdDTOMap.put(product.getId(), product);
            if (product.getStockVo().getStock() < orderRequestStoreProduct.getCartNum()) {
                throw new ServiceException("商品库存不足～");
            }
            BigDecimal p = BigDecimal.ZERO;
            if (groupShopId != null && groupShopPrice != null) {
                p = groupShopPrice;
            } else {
                p = product.getStockVo().getPrice().multiply(BigDecimal.valueOf(orderRequestStoreProduct.getCartNum()));
            }
            productPrice = productPrice.add(p);
            productOriginalPrice = productOriginalPrice.add(product.getOtPrice().multiply(BigDecimal.valueOf(orderRequestStoreProduct.getCartNum())));
            List<Long> categoryFamily = categoryService.getCategoryFamily(product.getCateId());
            for (Long cid : categoryFamily) {
                BigDecimal price = categoryPriceMap.get(cid);
                if (price == null) {
                    price = p;
                } else {
                    price = price.add(p);
                }
                categoryPriceMap.put(cid, price);
            }
        }
//                  这层判断会提交不了订单
//                if (skuPric!= orderRequest.getTotalPrice()) {
//                    throw new ServiceException(ExceptionDefinition.ORDER_PRICE_CHECK_FAILED);
//                }
        //优惠券折扣价格
        BigDecimal couponPrice = BigDecimal.ZERO;

        //参数强校验 END
        //???是否校验actualPrice??强迫校验？
        BigDecimal actualPrice = productPrice.subtract(couponPrice);
        OrderPriceBo orderPriceBo = new OrderPriceBo();
        orderPriceBo.setGroupShopId(groupShopId);
        orderPriceBo.setActualPrice(actualPrice);

        orderPriceBo.setFreightPrice(BigDecimal.ZERO);
        orderPriceBo.setProductTotalPrice(productPrice);
        orderPriceBo.setProductIdDTOMap(productIdDTOMap);
        orderPriceBo.setProductOriginalTotalPrice(productOriginalPrice);
        return orderPriceBo;
    }

    /**
     * 3.构建订单部分
     *
     * @param orderDO
     * @param orderRequest
     * @param channel
     * @param userId
     */
    @Override
    public void buildOrderHandlePart(StoreOrder orderDO, OrderPriceBo orderPriceDTO, OrderRequestBo orderRequest, String channel, Long userId) throws ServiceException {
        orderDO.setTotalPrice(orderPriceDTO.getProductTotalPrice());
        orderDO.setOriginalTotalPrice(orderPriceDTO.getProductOriginalTotalPrice());
        orderDO.setIsChannel(channel);
        orderDO.setPayPrice(orderPriceDTO.getActualPrice());
        orderDO.setCombinationId(orderPriceDTO.getGroupShopId());
        BigDecimal couponPrice = orderPriceDTO.getCouponPrice();
        if (couponPrice != null && couponPrice.compareTo(BigDecimal.ZERO) != 0) {
            orderDO.setCouponId(orderPriceDTO.getCouponId());
            orderDO.setCouponPrice(couponPrice);
        }
        Date now = new Date();
        orderDO.setRemark(orderRequest.getMono());
        orderDO.setFreightPrice(orderPriceDTO.getFreightPrice());
        orderDO.setOrderId(GeneratorUtil.genOrderId());
        orderDO.setUid(userId);
        orderDO.setStatus(OrderStatusType.UNPAY.getCode());
        orderDO.setUpdateTime(now);
        orderDO.setCreateTime(now);
        orderDO.setPredictDate(orderRequest.getPredictDate());
        if (orderRequest.getAddressId() != null) {
            Address address = addressMapper.selectById(orderRequest.getAddressId());
            if (!userId.equals(address.getUserId())) {
                throw new ServiceException("收货地址不属于您！");
            }
            orderDO.setRealName(address.getConsignee());
            orderDO.setUserPhone(address.getPhone());
            orderDO.setProvince(address.getProvince());
            orderDO.setCity(address.getCity());
            orderDO.setCounty(address.getCounty());
            orderDO.setUserAddress(address.getAddress());
            //添加预计送达时间
            orderDO.setPredictTime(orderRequest.getPredictTime());
            orderDO.setStoreId(orderRequest.getStorageId());
        }
        orderMapper.insert(orderDO);
    }

    /**
     * 4.更新优惠券部分
     *
     * @param orderDO
     */
    @Override
    public void buildCoupontHandlePart(StoreOrder orderDO) throws ServiceException {
        //扣除用户优惠券
//        if (orderDO.getCouponId() != null) {
//            UserCouponDO updateUserCouponDO = new UserCouponDO();
//            updateUserCouponDO.setId(orderDO.getCouponId());
//            updateUserCouponDO.setGmtUsed(new Date());
//            updateUserCouponDO.setOrderId(orderDO.getId());
//            userCouponMapper.updateById(updateUserCouponDO);
//        }
    }

    /**
     * 5.订单商品SKU部分
     */
    @Override
    public void buildOrderSkuHandlePart(StoreOrder orderDO, OrderPriceBo orderPriceDTO, OrderRequestBo orderRequest) {
        StoreProductVo productVo;
        StoreOrderProduct storeOrderProduct;
        Date now = new Date();
        Map<Long, StoreProductVo> productIdDTOMap = orderPriceDTO.getProductIdDTOMap();
        List<StoreOrderProduct> orderSkuDOList = new ArrayList<>();
        List<OrderRequestProductBo> skuList = orderRequest.getProductList();
        for (OrderRequestProductBo orderRequestStoreProduct : skuList) {
            storeOrderProduct = new StoreOrderProduct();
            productVo = productIdDTOMap.get(orderRequestStoreProduct.getProductId());
            storeOrderProduct.setBarCode(productVo.getBarCode());
            storeOrderProduct.setProductTitle(productVo.getStoreName());
            storeOrderProduct.setUnitName(productVo.getUnitName());
            storeOrderProduct.setProductAttrTitle(productVo.getStoreName());
            storeOrderProduct.setImg(productVo.getImage() == null ? productVo.getSliderImage() : productVo.getImage());
            storeOrderProduct.setNum(orderRequestStoreProduct.getCartNum());
            storeOrderProduct.setOtPrice(productVo.getOtPrice());
            storeOrderProduct.setPrice(productVo.getStockVo().getPrice());
            storeOrderProduct.setProductAttrId(productVo.getId());
            storeOrderProduct.setProductId(productVo.getId());
            storeOrderProduct.setOrderNo(orderDO.getOrderId());
            storeOrderProduct.setOrderId(orderDO.getId());
            storeOrderProduct.setCreateTime(now);
            storeOrderProduct.setUpdateTime(now);
            orderSkuDOList.add(storeOrderProduct);
            //扣除库存
            productMapper.decSkuStock(orderRequestStoreProduct.getProductId(), orderRequestStoreProduct.getCartNum(), orderDO.getStoreId());
        }
        orderProductMapper.insertBatch(orderSkuDOList);
    }

    /**
     * 6.购物车部分
     *
     * @param orderRequest
     * @param userId
     */
    @Override
    public void buildCartHandlePart(OrderRequestBo orderRequest, Long userId) {
        List<OrderRequestProductBo> skuList = orderRequest.getProductList();
        if (!StringUtils.isEmpty(orderRequest.getTakeWay())) {
            String takeWay = orderRequest.getTakeWay();
            if ("cart".equals(takeWay)) {
                //扣除购物车
                List<Long> productIds = skuList.stream().map(item -> item.getProductId()).collect(Collectors.toList());
                cartMapper.delete(new QueryWrapper<StoreCart>().in("product_id", productIds).eq("uid", userId));
            }
            //直接购买传值为 "buy"
        }
    }

    /**
     * 7.触发订单创建完成后通知事件部分
     *
     * @param orderDO
     */
    @Override
    public void buildNotifyHandlePart(StoreOrder orderDO) {
        OrderUpdater updater = new OrderUpdater(orderDO);
        observeAble.notifyObservers(updater);
    }
}
