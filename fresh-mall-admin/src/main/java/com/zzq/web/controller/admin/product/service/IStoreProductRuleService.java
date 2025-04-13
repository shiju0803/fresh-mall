package com.zzq.web.controller.admin.product.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.product.domain.bo.StoreProductRuleBo;
import com.zzq.product.domain.vo.StoreProductRuleVo;

import java.util.Collection;
import java.util.List;

/**
 * 商品规格Service接口
 */
public interface IStoreProductRuleService {

    /**
     * 查询商品规格
     */
    StoreProductRuleVo queryById(Long id);

    /**
     * 查询商品规格列表
     */
    TableDataInfo<StoreProductRuleVo> queryPageList(StoreProductRuleBo bo, PageQuery pageQuery);

    /**
     * 查询商品规格列表
     */
    List<StoreProductRuleVo> queryList(StoreProductRuleBo bo);

    /**
     * 新增商品规格
     */
    Boolean insertByBo(StoreProductRuleBo bo);

    /**
     * 修改商品规格
     */
    Boolean updateByBo(StoreProductRuleBo bo);

    /**
     * 校验并批量删除商品规格信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    List<StoreProductRuleVo> queryListAll();
}
