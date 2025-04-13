package com.zzq.web.controller.app.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.BeanCopyUtils;
import com.zzq.order.domain.StoreCart;
import com.zzq.order.domain.vo.StoreCartVo;
import com.zzq.order.mapper.StoreCartMapper;
import com.zzq.web.controller.app.order.service.IAppCartService;
import com.zzq.web.controller.app.product.service.IAppCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AppCartService implements IAppCartService {

    private final StoreCartMapper baseMapper;

    private final IAppCategoryService appCategoryService;

    @Override
    public Long countCart(Long storageId, Long userId) {
        Long userCountCart = baseMapper.countCart(userId,storageId);
        return (userCountCart == null) ? 0L : userCountCart;
    }

    @Override
    public List<StoreCartVo> getCartList(Long storageId, Long userId) {
        List<StoreCartVo> cartList = baseMapper.getCartList(userId, storageId);
        for (StoreCartVo cartVo : cartList) {
            cartVo.setCateIdList(appCategoryService.getCategoryFamily(cartVo.getCateId()));
        }
        return cartList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public StoreCartVo addCartItem(Long productId, Long num, Long userId) {
        List<StoreCart> storeCartList = baseMapper.selectList(
            new QueryWrapper<StoreCart>()
                .eq("product_id", productId)
                .eq("uid", userId));
        StoreCart storeCart = new StoreCart();
        Date now = new Date();
        if (!CollectionUtils.isEmpty(storeCartList)) {
            //若非空
            storeCart.setId(storeCartList.get(0).getId());
            storeCart.setCartNum(storeCartList.get(0).getCartNum() + num);
            storeCart.setUpdateTime(now);
            if (baseMapper.updateById(storeCart) <= 0) {
                throw new ServiceException("购物车更新失败!");
            }
        } else {
            //不存在，则添加购物车
            storeCart.setProductId(productId);
            storeCart.setCartNum(num);
            storeCart.setUid(userId);
            storeCart.setUpdateTime(now);
            storeCart.setCreateTime(now);
            if (baseMapper.insert(storeCart) <= 0) {
                throw new ServiceException("购物车更新失败!");
            }
        }
        StoreCartVo cartVo = new StoreCartVo();
        BeanCopyUtils.copy(storeCart, cartVo);
        return cartVo;
    }

    @Override
    public Long updateCartItemNum(Long cartId, Long num, Long userId) {
        StoreCart cart = new StoreCart();
        cart.setCartNum(num);
        int update = baseMapper.update(cart,
            new QueryWrapper<StoreCart>()
                .eq("id", cartId)
                .eq("uid", userId));
        if (update > 0) {
            return num;
        }
        throw new ServiceException("购物车更新失败!");
    }

    @Override
    public Boolean removeCartItem(Long cartId, Long userId) {
        return baseMapper.delete(
            new QueryWrapper<StoreCart>()
                .eq("id", cartId)
                .eq("uid", userId)) > 0;
    }
}
