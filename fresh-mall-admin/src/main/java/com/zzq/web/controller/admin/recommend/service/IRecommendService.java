package com.zzq.web.controller.admin.recommend.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.recommend.domain.bo.RecommendBo;
import com.zzq.recommend.domain.vo.RecommendVo;

import java.util.Collection;
import java.util.List;

/**
 * 推荐管理Service接口
 */
public interface IRecommendService {

    /**
     * 查询推荐管理
     */
    RecommendVo queryById(Long id);

    /**
     * 查询推荐管理列表
     */
    TableDataInfo<RecommendVo> queryPageList(RecommendBo bo, PageQuery pageQuery);

    /**
     * 查询推荐管理列表
     */
    List<RecommendVo> queryList(RecommendBo bo);

    /**
     * 新增推荐管理
     */
    Boolean insertByBo(RecommendBo bo);

    /**
     * 修改推荐管理
     */
    Boolean updateByBo(RecommendBo bo);

    /**
     * 校验并批量删除推荐管理信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);


    /**
     * 批量新增
     * @param boList
     * @return
     */
    Boolean addRecommendBatch(RecommendBo bo);
}
