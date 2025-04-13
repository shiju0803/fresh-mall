package com.zzq.web.controller.app.product.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.ProductStatusType;
import com.zzq.common.exception.ServiceException;
import com.zzq.order.domain.vo.StoreAppraiseVo;
import com.zzq.product.domain.StoreCategory;
import com.zzq.product.domain.StoreProduct;
import com.zzq.product.domain.vo.StoreProductVo;
import com.zzq.product.mapper.StoreProductMapper;
import com.zzq.storage.domain.Stock;
import com.zzq.storage.domain.vo.StockVo;
import com.zzq.storage.mapper.StockMapper;
import com.zzq.web.controller.app.order.service.IAppAppraiseService;
import com.zzq.web.controller.app.product.service.IAppCategoryService;
import com.zzq.web.controller.app.product.service.IAppProductService;
import com.zzq.web.controller.app.user.service.IUserCollectService;
import com.zzq.web.controller.app.user.service.IUserFootprintService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.LinkedList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AppProductService implements IAppProductService {

    private final StoreProductMapper baseMapper;

    private final IAppCategoryService categoryService;

    private final IAppAppraiseService appraiseService;

    private final IUserFootprintService userFootprintService;

    private final StockMapper stockMapper;

    private final IUserCollectService iUserCollectService;


    /**
     * PRODUCT 分页缓存
     */
    public static final String CA_PRODUCT_PAGE_PREFIX = "CA_PRODUCT_PAGE_";

    /**
     * 缓存  CA_PRODUCT_+productId
     */
    public static final String CA_PRODUCT_PREFIX = "CA_PRODUCT_";

    @Override
    public TableDataInfo<StoreProductVo> getGoodsPage(Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title) {

        LambdaQueryWrapper<StoreProduct> wrapper = new LambdaQueryWrapper<>();

        if (StrUtil.isNotBlank(title)) {
            wrapper.like(StoreProduct::getStoreName, title);
        }
//        else {
//            //若关键字为空，尝试从缓存取列表
//            TableDataInfo<StoreProductVo> objFromCache = RedisUtils.getCacheObject(CA_PRODUCT_PAGE_PREFIX + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc);
//            if (objFromCache != null) {
//                return objFromCache;
//            }
//        }

        if (orderBy != null && isAsc != null) {
            wrapper.last("ORDER BY " + orderBy + (isAsc ? " ASC" : " DESC"));
        }

        if (categoryId != null && categoryId != 0L) {

            List<StoreCategory> childrenList = categoryService.selectList(new LambdaQueryWrapper<StoreCategory>().eq(StoreCategory::getPid, categoryId));

            if (CollectionUtils.isEmpty(childrenList)) {
                //目标节点为叶子节点,即三级类目
                wrapper.eq(StoreProduct::getCateId, categoryId);
            } else {
                //目标节点存在子节点
                LinkedList<Long> childrenIds = new LinkedList<>();
                StoreCategory category = categoryService.selectById(categoryId);

                // 检验传入类目是一级还是二级类目
                if (category.getPid() != 0L) {
                    //二级分类
                    childrenList.forEach(item -> {
                        childrenIds.add(item.getId());
                    });
                } else {
                    //一级分类
                    childrenList.forEach(item -> {
                        List<StoreCategory> leafList = categoryService.selectList(new LambdaQueryWrapper<StoreCategory>().eq(StoreCategory::getPid, item.getId()));
                        if (!CollectionUtils.isEmpty(leafList)) {
                            leafList.forEach(leafItem -> {
                                childrenIds.add(leafItem.getId());
                            });
                        }
                    });
                }
                wrapper.in(StoreProduct::getCateId, childrenIds);
            }
        }

        wrapper.eq(StoreProduct::getIsShow, ProductStatusType.SELLING.getCode());

        PageQuery pageQuery = new PageQuery();
        pageQuery.setPageNum(pageNo - 1);
        pageQuery.setPageSize(pageSize);

        Page<StoreProductVo> productPage = baseMapper.selectVoPage(pageQuery.build(), wrapper);

        TableDataInfo<StoreProductVo> tableDataInfo = TableDataInfo.build(productPage);

//        if (StringUtils.isEmpty(title)) {
//            //若关键字为空，制作缓存
//            RedisUtils.setCacheObject(CA_PRODUCT_PAGE_PREFIX + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc, tableDataInfo);
//        }
        return tableDataInfo;
    }

    @Override
    public TableDataInfo<StoreProductVo> getGoodsPageByStorage(Long storageId, Integer pageNo, Integer pageSize, Long categoryId, String orderBy, Boolean isAsc, String title,Integer type) {
//        //缓存key
//        String keyCache = CA_PRODUCT_PAGE_PREFIX + storageId + "_" + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc;
//        //开始组装条件search
//        if (StringUtils.isEmpty(title)) {
//            //若关键字为空，尝试从缓存取列表
//            TableDataInfo<StoreProductVo> objFromCache = RedisUtils.getCacheObject(keyCache);
//            if (objFromCache != null) {
//                //return objFromCache;
//            }
//        }
        if (orderBy != null && isAsc != null) {
            orderBy = "b."+orderBy;
        }
        LinkedList<Long> childrenIds = new LinkedList<>();
        if (categoryId != null && categoryId != 0L) {

            List<StoreCategory> childrenList = categoryService.selectList(new LambdaQueryWrapper<StoreCategory>().eq(StoreCategory::getPid, categoryId));
            if (!CollectionUtils.isEmpty(childrenList)) {
                //一级分类
                childrenList.forEach(item -> childrenIds.add(item.getId()));
                //使用in查询，就不需要等于查询
                categoryId = null;
            }
        }

        Integer offset = (pageNo - 1) * pageSize;
        Integer size = pageSize;

        List<StoreProductVo> productPage = baseMapper.selectPageByStorage(offset,size, title, categoryId, childrenIds, storageId, orderBy, isAsc,type);

        Long count = baseMapper.selectPageByStorageCount(title, categoryId, childrenIds, storageId, orderBy, isAsc);

        TableDataInfo<StoreProductVo> tableDataInfo = new TableDataInfo<>(productPage,count);

//        if (StringUtils.isEmpty(title)) {
//            //若关键字为空，制作缓存
//            RedisUtils.setCacheObject(CA_PRODUCT_PAGE_PREFIX + categoryId + "_" + pageNo + "_" + pageSize + "_" + orderBy + "_" + isAsc, tableDataInfo);
//        }
        return tableDataInfo;
    }

    @Override
    public StoreProductVo getGoodsByStorage(Long storageId,Long productId, Long userId) {
//        //获取缓存
//        StoreProductVo storeProductVo = RedisUtils.getCacheObject(CA_PRODUCT_PREFIX + storageId + "_" + productId);
//        if (storeProductVo != null) {
//            //获取第一页评论
//            List<StoreAppraiseVo> storeAppraisePage = appraiseService.getProductAppraiseByPage(productId, 1, 10, 1).getRows();
//            storeProductVo.setAppraisePage(storeAppraisePage);
//            //新增该用户查看印记
//            if (userId != null) {
//                userFootprintService.addOrUpdateFootprint(userId, productId);
//            }
//            //查询库存信息
//            Stock kxStock = new Stock();
//            kxStock.setProductId(productId);
//            kxStock.setStorageId(storageId);
//            kxStock = stockMapper.selectOne(new QueryWrapper<>(kxStock));
//
//            StockVo stockVo = new StockVo();
//            if (!ObjectUtils.isEmpty(kxStock)) {
//                BeanUtils.copyProperties(kxStock, stockVo);
//            }
//            storeProductVo.setStockVo(stockVo);
//            return storeProductVo;
//        }
        //是否为活动商品
        StoreProduct product  = baseMapper.selectById(productId);
        if (ObjectUtils.isEmpty(product)) {
            throw new ServiceException("商品对象不存在");
        }
        StoreProductVo kxStoreProductVo = new StoreProductVo();
        BeanUtils.copyProperties(product, kxStoreProductVo);


        //查询库存信息
        Stock kxStock = new Stock();
        kxStock.setProductId(productId);
        kxStock.setStorageId(storageId);
        kxStock = stockMapper.selectOne(new QueryWrapper<>(kxStock));

        StockVo stockVo = new StockVo();
        if (!ObjectUtils.isEmpty(kxStock)) {
            BeanUtils.copyProperties(kxStock, stockVo);
        }
        kxStoreProductVo.setStockVo(stockVo);
        //类目族
        kxStoreProductVo.setCateIdList(categoryService.getCategoryFamily(product.getCateId()));

        //放入缓存
//        RedisUtils.setCacheObject(CA_PRODUCT_PREFIX + storageId + "_" + productId, kxStoreProductVo);
        //获取第一页评论
        List<StoreAppraiseVo> storeAppraisePage = appraiseService.getProductAppraiseByPage(productId, 1, 10, 1).getRows();
        kxStoreProductVo.setAppraisePage(storeAppraisePage);
        if (userId != null) {
            userFootprintService.addOrUpdateFootprint(userId, productId);
            // 查询收藏信息
            Long l = iUserCollectService.queryByIdAndProductId(userId, productId);
            kxStoreProductVo.setIsCollection(l.intValue() > 0 ? 1 : 0);
        }
        return kxStoreProductVo;
    }
}
