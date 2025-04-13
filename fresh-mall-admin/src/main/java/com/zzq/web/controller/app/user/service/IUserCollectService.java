package com.zzq.web.controller.app.user.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserCollectBo;
import com.zzq.user.domain.vo.UserCollectVo;

import java.util.Collection;
import java.util.List;

/**
 * 客户收藏Service接口
 * @date 2023-04-06
 */
public interface IUserCollectService {

    /**
     * 查询客户收藏
     */
    UserCollectVo queryById(Long id);

    /**
     * 查询客户是否收藏某商品
     */
    Long queryByIdAndProductId(Long userId, Long productId);

    /**
     * 查询客户收藏列表
     */
    TableDataInfo<UserCollectVo> queryPageList(UserCollectBo bo, PageQuery pageQuery);

    /**
     * 查询客户收藏列表
     */
    List<UserCollectVo> queryList(UserCollectBo bo);

    /**
     * 新增客户收藏
     */
    Boolean insertByBo(UserCollectBo bo);

    /**
     * 修改客户收藏
     */
    Boolean updateByBo(UserCollectBo bo);

    /**
     * 校验并批量删除客户收藏信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 删除客户收藏
     * @param bo
     * @return
     */
    int deleteCollect(UserCollectBo bo);

    /**
     * 查询某一用户收藏记录
     * @param bo
     * @param pageQuery
     * @return
     */
    TableDataInfo<UserCollectVo> getCollectAll(UserCollectBo bo, PageQuery pageQuery);
}
