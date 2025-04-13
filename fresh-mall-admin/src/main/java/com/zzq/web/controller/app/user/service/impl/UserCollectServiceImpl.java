package com.zzq.web.controller.app.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.UserCollect;
import com.zzq.user.domain.bo.UserCollectBo;
import com.zzq.user.domain.vo.UserCollectVo;
import com.zzq.user.mapper.UserCollectMapper;
import com.zzq.web.controller.app.user.service.IUserCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 客户收藏Service业务层处理
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class UserCollectServiceImpl implements IUserCollectService {

    private final UserCollectMapper baseMapper;

    /**
     * 查询客户收藏
     */
    @Override
    public UserCollectVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询客户是否收藏某商品
     *
     * @param userId
     * @param productId
     */
    @Override
    public Long queryByIdAndProductId(Long userId, Long productId) {
        return baseMapper.selectCount(new LambdaQueryWrapper<UserCollect>()
                .eq(UserCollect::getUserId, userId)
                .eq(UserCollect::getProductId, productId));
    }

    /**
     * 查询客户收藏列表
     */
    @Override
    public TableDataInfo<UserCollectVo> queryPageList(UserCollectBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<UserCollect> lqw = buildQueryWrapper(bo);
        Page<UserCollectVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询客户收藏列表
     */
    @Override
    public List<UserCollectVo> queryList(UserCollectBo bo) {
        LambdaQueryWrapper<UserCollect> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<UserCollect> buildQueryWrapper(UserCollectBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<UserCollect> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getUserId() != null, UserCollect::getUserId, bo.getUserId());
        lqw.eq(bo.getProductId() != null, UserCollect::getProductId, bo.getProductId());
        return lqw;
    }

    /**
     * 新增客户收藏
     */
    @Override
    public Boolean insertByBo(UserCollectBo bo) {
        UserCollect add = BeanUtil.toBean(bo, UserCollect.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改客户收藏
     */
    @Override
    public Boolean updateByBo(UserCollectBo bo) {
        UserCollect update = BeanUtil.toBean(bo, UserCollect.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(UserCollect entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除客户收藏
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public int deleteCollect(UserCollectBo bo) {
        return baseMapper.delete(new LambdaQueryWrapper<UserCollect>()
                    .eq(UserCollect::getUserId, bo.getUserId())
                    .eq(UserCollect::getProductId, bo.getProductId()));
    }


    @Override
    public TableDataInfo<UserCollectVo> getCollectAll(UserCollectBo bo, PageQuery pageQuery) {
        Integer offset = (pageQuery.getPageNum() - 1) * pageQuery.getPageSize();
        Integer size = pageQuery.getPageSize();

        List<UserCollectVo> collectAll = baseMapper.getCollectAll(bo.getUserId(), offset, size);
        Long count = baseMapper.getCollectAllByCount(bo.getUserId());
        return new TableDataInfo<>(collectAll, count);
    }
}
