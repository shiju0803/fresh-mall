package com.zzq.web.controller.admin.product.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.product.domain.bo.StoreProductBo;
import com.zzq.product.domain.vo.ProductTreeNodeVo;
import com.zzq.product.domain.vo.StoreProductVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品Service接口
 */
public interface IStoreProductService {

    /**
     * 查询商品
     */
    Map<String,Object> queryById(Long id);

    /**
     * 查询商品列表
     */
    TableDataInfo<StoreProductVo> queryPageList(StoreProductBo bo, PageQuery pageQuery);

    /**
     * 查询商品列表
     */
    List<StoreProductVo> queryList(StoreProductBo bo);

    /**
     * 新增商品
     */
    Boolean insertAndupdateByBo(StoreProductBo bo);

    /**
     * 修改商品
     */
    Boolean updateByBo(StoreProductBo bo);

    /**
     * 校验并批量删除商品信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Long selectCountByCateId(Long CateId);

    /**
     * 商品上下架
     * @param id
     * @param status
     */
    void onSale(Long id, Integer status);

    Map<String, Object> getFormatAttr(Long id, String jsonStr, boolean isActivity);

    /**
     * 查询数量
     * @param productId
     * @return
     */
    Long selectCountById(Long productId);

    /**
     * 授权
     * @param bo
     * @return
     */
    Boolean batchAuthorizeGoods(StoreProductBo bo);

    /**
     * 获取产品树
     * @return
     */
    List<ProductTreeNodeVo> getProductBigTree();

}
