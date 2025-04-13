package com.zzq.web.controller.admin.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.order.domain.StoreAppraise;
import com.zzq.order.domain.bo.StoreAppraiseBo;
import com.zzq.order.domain.vo.StoreAppraiseVo;
import com.zzq.order.mapper.StoreAppraiseMapper;
import com.zzq.web.controller.admin.order.service.IStoreAppraiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.zzq.common.constant.ProductCacheConstants.CA_APPRAISE_KEY;

/**
 * 评论管理Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class StoreAppraiseServiceImpl implements IStoreAppraiseService {

    private final StoreAppraiseMapper baseMapper;

    /**
     * 查询评论管理
     */
    @Override
    public StoreAppraiseVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询评论管理列表
     */
    @Override
    public TableDataInfo<StoreAppraiseVo> queryPageList(StoreAppraiseBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<StoreAppraise> lqw = buildQueryWrapper(bo);
        Page<StoreAppraiseVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询评论管理列表
     */
    @Override
    public List<StoreAppraiseVo> queryList(StoreAppraiseBo bo) {
        LambdaQueryWrapper<StoreAppraise> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<StoreAppraise> buildQueryWrapper(StoreAppraiseBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<StoreAppraise> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProductId() != null, StoreAppraise::getProductId, bo.getProductId());
        lqw.eq(bo.getProductAttrId() != null, StoreAppraise::getProductAttrId, bo.getProductAttrId());
        lqw.eq(bo.getOrderId() != null, StoreAppraise::getOrderId, bo.getOrderId());
        lqw.eq(bo.getUserId() != null, StoreAppraise::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getContent()), StoreAppraise::getContent, bo.getContent());
        lqw.eq(bo.getScore() != null, StoreAppraise::getScore, bo.getScore());
        lqw.eq(bo.getState() != null, StoreAppraise::getState, bo.getState());
        return lqw;
    }

    /**
     * 新增评论管理
     */
    @Override
    public Boolean insertByBo(StoreAppraiseBo bo) {
        StoreAppraise add = BeanUtil.toBean(bo, StoreAppraise.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改评论管理
     */
    @Override
    public Boolean updateByBo(StoreAppraiseBo bo) {
        StoreAppraise update = BeanUtil.toBean(bo, StoreAppraise.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(StoreAppraise entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除评论管理
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public int changeState(Long id) {
        StoreAppraise byId = baseMapper.selectById(id);

        StoreAppraise update = new StoreAppraise();
        update.setId(id);
        update.setState(1L);
        baseMapper.updateById(update);
        RedisUtils.deleteKeys(CA_APPRAISE_KEY + byId.getProductId() + "*"); //删除商品评论缓存
        return 1;
    }
}
