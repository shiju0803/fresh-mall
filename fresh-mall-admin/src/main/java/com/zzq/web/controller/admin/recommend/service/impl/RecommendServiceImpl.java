package com.zzq.web.controller.admin.recommend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.recommend.domain.Recommend;
import com.zzq.recommend.domain.bo.RecommendBo;
import com.zzq.recommend.domain.vo.RecommendVo;
import com.zzq.recommend.mapper.RecommendMapper;
import com.zzq.web.controller.admin.product.service.IStoreProductService;
import com.zzq.web.controller.admin.recommend.service.IRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.zzq.common.constant.ProductCacheConstants.RECOMMEND_NAME;

/**
 * 推荐管理Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class RecommendServiceImpl implements IRecommendService {

    private final RecommendMapper baseMapper;

    private final IStoreProductService productService;


    /**
     * 查询推荐管理
     */
    @Override
    public RecommendVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询推荐管理列表
     */
    @Override
    public TableDataInfo<RecommendVo> queryPageList(RecommendBo bo, PageQuery pageQuery) {
        Wrapper<Recommend> wrapper = buildQueryWrapperAlias(bo);
        Page<RecommendVo> result = baseMapper.selectVoPageBySQL(pageQuery.build(), wrapper);
        return TableDataInfo.build(result);
    }

    /**
     * 查询推荐管理列表
     */
    @Override
    public List<RecommendVo> queryList(RecommendBo bo) {
        LambdaQueryWrapper<Recommend> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Recommend> buildQueryWrapper(RecommendBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Recommend> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, Recommend::getProductId, bo.getProductId());
        lqw.eq(bo.getRecommendType() != null, Recommend::getRecommendType, bo.getRecommendType());
        return lqw;
    }
    private Wrapper<Recommend> buildQueryWrapperAlias(RecommendBo bo) {
        QueryWrapper<Recommend> wrapper = Wrappers.query();
        wrapper.eq(bo.getRecommendType() != null,"re.recommend_type", bo.getRecommendType());
        return wrapper;
    }

    /**
     * 新增推荐管理
     */
    @Override
    public Boolean insertByBo(RecommendBo bo) {
        if (productService.selectCountById(bo.getProductId()) > 0L) {
            throw new ServiceException("你要加入推荐的商品不存在");
        }

        if (baseMapper.selectCount(new LambdaQueryWrapper<Recommend>()
            .eq(Recommend::getProductId, bo.getProductId())
            .eq(Recommend::getRecommendType, bo.getRecommendType())) > 0) {
            throw new ServiceException("你要加入推荐的商品已推荐");
        }
        Recommend add = BeanUtil.toBean(bo, Recommend.class);
        Date now = new Date();
        add.setUpdateTime(now);
        add.setCreateTime(now);
        if (!(baseMapper.insert(add) > 0)) {
            throw new ServiceException("加入推荐数据库失败");
        }
        RedisUtils.deleteObject(RECOMMEND_NAME + bo.getRecommendType());
        return true;

    }

    /**
     * 修改推荐管理
     */
    @Override
    public Boolean updateByBo(RecommendBo bo) {
        Recommend update = BeanUtil.toBean(bo, Recommend.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Recommend entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除推荐管理
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        List<Recommend> recommends = baseMapper.selectList(new LambdaQueryWrapper<Recommend>().in(Recommend::getId, ids));
        for (Recommend recommend : recommends) {
            if (baseMapper.deleteById(recommend.getId()) > 0) {
                RedisUtils.deleteKeys(RECOMMEND_NAME + "*");
            }
        }
        return true;
    }

    @Override
    public Boolean addRecommendBatch(RecommendBo bo) {

        if (CollectionUtils.isEmpty(bo.getProductIds())) {
            throw new ServiceException("你要加入推荐的商品不存在");
        }

        baseMapper.delete(new LambdaQueryWrapper<Recommend>()
            .in(Recommend::getProductId, bo.getProductIds())
            .eq(Recommend::getRecommendType, bo.getRecommendType()));

        List<Recommend> recommendDOList = new ArrayList<>();
        for (Long productId : bo.getProductIds()) {
            Recommend add = new Recommend();
            add.setProductId(productId);
            add.setRecommendType(bo.getRecommendType());
            Date now = new Date();
            add.setUpdateTime(now);
            add.setCreateTime(now);
            recommendDOList.add(add);
        }
        baseMapper.insertBatch(recommendDOList);
        RedisUtils.deleteObject(RECOMMEND_NAME+bo.getRecommendType());
        return true;
    }
}
