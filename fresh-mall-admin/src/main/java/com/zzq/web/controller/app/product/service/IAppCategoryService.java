package com.zzq.web.controller.app.product.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzq.product.domain.StoreCategory;
import com.zzq.product.domain.vo.StoreCategoryVo;

import java.util.List;

public interface IAppCategoryService {

    /**
     *
     * @param cartId
     * @return
     */
    List<Long> getCategoryFamily(Long cateId);

    /**
     * 查询分类列表
     * @param eq
     * @return
     */
    List<StoreCategory> selectList(LambdaQueryWrapper<StoreCategory> eq);

    /**
     * 获取分类对象
     * @param categoryId
     * @return
     */
    StoreCategory selectById(Long categoryId);

    /**
     * 查询所有目录
     *
     * @return
     */
    List<StoreCategoryVo> categoryList();
}
