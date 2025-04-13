package com.zzq.web.controller.admin.product.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.product.domain.StoreCategory;
import com.zzq.product.domain.bo.StoreCategoryBo;
import com.zzq.product.domain.vo.StoreCategoryVo;
import com.zzq.product.mapper.StoreCategoryMapper;
import com.zzq.web.controller.admin.product.service.IStoreCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品分类Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreCategoryServiceImpl implements IStoreCategoryService {

    private final StoreCategoryMapper baseMapper;

    public static final String CA_CATEGORY_LIST = "CA_CATEGORY_LIST";

    /**
     * 查询商品分类
     */
    @Override
    public StoreCategoryVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询商品分类列表
     */
    @Override
    public TableDataInfo<StoreCategoryVo> queryPageList(StoreCategoryBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreCategory> lqw = buildQueryWrapper(bo);
        Page<StoreCategoryVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询商品分类列表
     */
    @Override
    public List<StoreCategoryVo> queryList(StoreCategoryBo bo) {
        LambdaQueryWrapper<StoreCategory> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreCategory> buildQueryWrapper(StoreCategoryBo bo) {
        LambdaQueryWrapper<StoreCategory> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getPid() != null, StoreCategory::getPid, bo.getPid());
        lqw.like(StringUtils.isNotBlank(bo.getCateName()), StoreCategory::getCateName, bo.getCateName());
        lqw.eq(StringUtils.isNotBlank(bo.getPic()), StoreCategory::getPic, bo.getPic());
        lqw.eq(bo.getIsShow() != null, StoreCategory::getIsShow, bo.getIsShow());
        lqw.in(bo.getPids() != null && !bo.getPids().isEmpty(), StoreCategory::getPid, bo.getPids());
        lqw.orderByAsc(StoreCategory::getSort);
        return lqw;
    }

    /**
     * 新增商品分类
     */
    @Override
    public Boolean insertByBo(StoreCategoryBo bo) {
        StoreCategory add = BeanUtil.toBean(bo, StoreCategory.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        //清除缓存
        RedisUtils.deleteObject(CA_CATEGORY_LIST);
        return flag;
    }

    /**
     * 修改商品分类
     */
    @Override
    public Boolean updateByBo(StoreCategoryBo bo) {
        StoreCategory update = BeanUtil.toBean(bo, StoreCategory.class);
        validEntityBeforeSave(update);
        //清除缓存
        RedisUtils.deleteObject(CA_CATEGORY_LIST);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreCategory entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除商品分类
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        //清除缓存
        RedisUtils.deleteObject(CA_CATEGORY_LIST);
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public Long selectCountByPid(Long id) {
        return baseMapper.selectCount(new LambdaQueryWrapper<StoreCategory>()
            .eq(StoreCategory::getPid, id));
    }

    @Override
    public boolean checkCategory(Long pid) {
        if(pid == 0) {
            return true;
        }
        StoreCategory kxStoreCategory = baseMapper.selectOne(new LambdaQueryWrapper<StoreCategory>()
            .eq(StoreCategory::getId,pid));
        return kxStoreCategory.getPid() <= 0;
    }

    @Override
    public Map<String, Object> buildTree(List<StoreCategoryVo> storeCategoryVos) {
        Set<StoreCategoryVo> trees = new LinkedHashSet<>();
        Set<StoreCategoryVo> cates = new LinkedHashSet<>();
        List<String> deptNames = storeCategoryVos.stream().map(StoreCategoryVo::getCateName)
            .collect(Collectors.toList());

        boolean isChild;
        List<StoreCategory> categories = baseMapper.selectList();
        for (StoreCategoryVo deptDTO : storeCategoryVos) {
            deptDTO.setLabel(deptDTO.getCateName());
            isChild = false;
            if ("0".equals(deptDTO.getPid().toString())) {
                trees.add(deptDTO);
            }
            for (StoreCategoryVo it : storeCategoryVos) {
                if (it.getPid().equals(deptDTO.getId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild) {
                cates.add(deptDTO);
            }
            for (StoreCategory category : categories) {
                if (category.getId().equals(deptDTO.getPid()) && !deptNames.contains(category.getCateName())) {
                    cates.add(deptDTO);
                }
            }
        }


        if (CollectionUtils.isEmpty(trees)) {
            trees = cates;
        }
        Integer totalElements = storeCategoryVos.size();

        Map<String, Object> map = new HashMap<>(2);
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? storeCategoryVos : trees);
        return map;
    }

    public List<StoreCategoryVo> buildPageTree(List<StoreCategoryVo> vos) {
        // 使用一个 Map 存储 id -> 节点
        Map<Long, StoreCategoryVo> categoryVoMap = new HashMap<>();
        for (StoreCategoryVo categoryVo : vos) {
            categoryVo.setLabel(categoryVo.getCateName());
            categoryVoMap.put(categoryVo.getId(), categoryVo);
        }

        // 存储最终的树结构
        List<StoreCategoryVo> tree = new ArrayList<>();

        // 构建树状结构
        for (StoreCategoryVo vo : vos) {
            Long parentId = vo.getPid();
            if (parentId == null || !categoryVoMap.containsKey(parentId)) {
                // 如果没有父节点（parentId 为空或找不到父节点），视为根节点
                tree.add(vo);
            } else {
                // 如果有父节点，将其添加到父节点的子节点列表
                StoreCategoryVo parent = categoryVoMap.get(parentId);
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                parent.getChildren().add(vo);
            }
        }
        return tree;
    }
}
