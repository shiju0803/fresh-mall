package com.zzq.product.mapper;

import com.zzq.product.domain.vo.StoreProductVo;
import com.zzq.product.domain.StoreProduct;
import com.zzq.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedList;
import java.util.List;

/**
 * 商品Mapper接口
 */
public interface StoreProductMapper extends BaseMapperPlus<StoreProductMapper, StoreProduct, StoreProductVo> {

    List<StoreProductVo> selectPageByStorage(@Param("offset") Integer offset,
                                             @Param("size") Integer size,
                                             @Param("title") String title,
                                             @Param("categoryId") Long categoryId,
                                             @Param("childrenIds") LinkedList<Long> childrenIds,
                                             @Param("storageId") Long storageId,
                                             @Param("orderBy") String orderBy,
                                             @Param("isAsc") Boolean isAsc,
                                             @Param("type") Integer type);

    Long selectPageByStorageCount(@Param("title") String title,
                                  @Param("categoryId") Long categoryId,
                                  @Param("childrenIds") LinkedList<Long> childrenIds,
                                  @Param("storageId") Long storageId,
                                  @Param("orderBy") String orderBy,
                                  @Param("isAsc") Boolean isAsc);

    List<StoreProduct> getProductTitleAll();


    StoreProductVo getProductByIdAndStorageId(@Param("productId") Long productId, @Param("storageId") Long storageId);

    /**
     * 库存扣减
     * @param productId
     * @param num
     * @param storeId
     * @return
     */
    Integer decSkuStock(@Param("productId") Long productId, @Param("num") Integer num, @Param("storeId") Long storeId);

    /**
     * 库存回扣
     * @param productId
     * @param num
     * @param storeId
     * @return
     */
    Integer restoreSkuStock(@Param("productId") Long productId, @Param("num") Integer num, @Param("storeId") Long storeId);

    /**
     * 增加销量
     * @param productId
     * @param num
     */
    void incSales(@Param("productId") Long productId, @Param("num") Integer num);


    /**
     * 获取产品通过仓库
     * @param storageId
     * @return
     */
    List<StoreProduct> getProductTitleAllByStorageId(@Param("storageId") Long storageId);

    /**
     * 秒杀库存扣减
     * @param productId
     * @param cartNum
     * @param storeId
     * @return
     */
    Integer decSeckillStock(@Param("productId") Long productId, @Param("seckillId") Long seckillId, @Param("cartNum") Integer cartNum, @Param("storeId") Long storeId);

    /**
     * 查询该仓库下的多规格
     * @param storageId
     * @param commonId
     * @return
     */
    List<StoreProduct> selectListByStorage(@Param("storageId") Long storageId, @Param("commonId") String commonId);
}
