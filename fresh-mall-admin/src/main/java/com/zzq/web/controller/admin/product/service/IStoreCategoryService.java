package com.zzq.web.controller.admin.product.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.product.domain.bo.StoreCategoryBo;
import com.zzq.product.domain.vo.StoreCategoryVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 商品分类Service接口
 */
public interface IStoreCategoryService {

    /**
     * 查询商品分类
     */
    StoreCategoryVo queryById(Long id);

    /**
     * 查询商品分类列表
     */
    TableDataInfo<StoreCategoryVo> queryPageList(StoreCategoryBo bo, PageQuery pageQuery);

    /**
     * 查询商品分类列表
     */
    List<StoreCategoryVo> queryList(StoreCategoryBo bo);

    /**
     * 新增商品分类
     */
    Boolean insertByBo(StoreCategoryBo bo);

    /**
     * 修改商品分类
     */
    Boolean updateByBo(StoreCategoryBo bo);

    /**
     * 校验并批量删除商品分类信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    Long selectCountByPid(Long id);

    /**
     * 检测分类是否操过二级
     * @param pid 父级id
     * @return boolean
     */
    boolean checkCategory(Long pid);

    /**
     * 构建树形
     * @param storeCategoryVos 分类列表
     * @return map
     */
    Map<String, Object> buildTree(List<StoreCategoryVo> storeCategoryVos);

    List<StoreCategoryVo> buildPageTree(List<StoreCategoryVo> rows);
}
