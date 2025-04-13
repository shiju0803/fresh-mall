package com.zzq.order.mapper;

import com.zzq.order.domain.StoreAppraise;
import com.zzq.order.domain.vo.StoreAppraiseVo;
import com.zzq.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论管理Mapper接口
 */
public interface StoreAppraiseMapper extends BaseMapperPlus<StoreAppraiseMapper, StoreAppraise, StoreAppraiseVo> {

    /**
     * 获取评论
     * @param productId
     * @param offset
     * @param pageSize
     * @return
     */
    List<StoreAppraiseVo> selectProductAppraiseByPage(@Param("productId") Long productId, @Param("offset") Integer offset, @Param("size") Integer pageSize);
}
