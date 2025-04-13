package com.zzq.web.controller.app.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzq.product.domain.StoreCategory;
import com.zzq.product.domain.vo.StoreCategoryVo;
import com.zzq.product.mapper.StoreCategoryMapper;
import com.zzq.web.controller.app.product.service.IAppCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AppCategoryService implements IAppCategoryService {

    private final StoreCategoryMapper baseMapper;

    public static final String CA_CATEGORY_ID_HASH = "CA_CATEGORY_ID_HASH";

    public static final String CA_CATEGORY_LIST = "CA_CATEGORY_LIST";

    @Override
    public List<Long> getCategoryFamily(Long cateId) {
//        Map<String, String> hashAll = RedisUtils.getCacheMap(CA_CATEGORY_ID_HASH);
        Map<String, String> hashAll = new HashMap<>();
        if (hashAll == null || ObjectUtils.isEmpty(hashAll)) {
            //构建此Hash表
            final Map<String,String> newHash = new HashMap<>();
            //将所有子节点查询出来
            List<StoreCategoryVo> categoryList = categoryList();
            categoryList.forEach(topItem -> {
                if (!CollectionUtils.isEmpty(topItem.getChildren()))
                    topItem.getChildren().forEach(subItem -> {
                        if (!CollectionUtils.isEmpty(subItem.getChildren()))
                            subItem.getChildren().forEach(leafItem -> {
                                newHash.put("S" + leafItem.getId(), subItem.getId() + "_" + topItem.getId());
                            });
                    });
            });
            hashAll = newHash;
//            RedisUtils.setCacheMap(CA_CATEGORY_ID_HASH, hashAll);
        }

        LinkedList<Long> ids = new LinkedList<>();
        ids.add(cateId);
        String str = hashAll.get("S" + cateId);
        if (!StringUtils.isEmpty(str)) {
            String[] split = str.split("_");
            ids.add(new Long(split[0]));
            ids.add(new Long(split[1]));
        }
        return ids;
    }

    @Override
    public List<StoreCategory> selectList(LambdaQueryWrapper<StoreCategory> eq) {
        return baseMapper.selectList(eq);
    }

    @Override
    public StoreCategory selectById(Long categoryId) {
        return baseMapper.selectById(categoryId);
    }


    @Override
    public List<StoreCategoryVo> categoryList() {
//        List<StoreCategoryVo> categoryDTOListFormCache = RedisUtils.getCacheList(CA_CATEGORY_LIST);
//        if (!CollectionUtils.isEmpty(categoryDTOListFormCache)) {
//            return categoryDTOListFormCache;
//        }
        //从数据库查询
        List<StoreCategory> categoryDOList = baseMapper.selectList(new LambdaQueryWrapper<>());
        //组装DTO
        List<StoreCategoryVo> storeCategoryVoLinkedList = new LinkedList<>();
        categoryDOList.forEach(categoryDO -> {
            if (categoryDO.getPid() == 0) {
                StoreCategoryVo categoryDTO = new StoreCategoryVo();
                BeanUtils.copyProperties(categoryDO, categoryDTO);
                storeCategoryVoLinkedList.add(categoryDTO);
            }
        });

        //遍历二、三级
        storeCategoryVoLinkedList.forEach(storeCategoryVo -> {
            categoryDOList.forEach(categoryDO -> {
                if (categoryDO.getPid().equals(storeCategoryVo.getId())) {
                    List<StoreCategoryVo> childrenList = storeCategoryVo.getChildren();
                    if (childrenList == null) {
                        childrenList = new LinkedList<>();
                        storeCategoryVo.setChildren(childrenList);
                    }
                    StoreCategoryVo childStoreCategoryVo = new StoreCategoryVo();
                    BeanUtils.copyProperties(categoryDO, childStoreCategoryVo);
                    childStoreCategoryVo.setChildren(new LinkedList<>());
                    childrenList.add(childStoreCategoryVo);
                    categoryDOList.forEach(leaf -> {
                        if (childStoreCategoryVo.getId().equals(leaf.getPid())) {
                            StoreCategoryVo leafStoreCategoryVo = new StoreCategoryVo();
                            BeanUtils.copyProperties(leaf, leafStoreCategoryVo);
                            childStoreCategoryVo.getChildren().add(leafStoreCategoryVo);
                        }
                    });
                }
            });
        });

        //放入缓存
//        RedisUtils.setCacheList(CA_CATEGORY_LIST, storeCategoryVoLinkedList);
        return storeCategoryVoLinkedList;
    }
}
