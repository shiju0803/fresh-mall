package com.zzq.user.mapper;

import com.zzq.user.domain.UserCollect;
import com.zzq.user.domain.vo.UserCollectVo;
import com.zzq.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 客户收藏Mapper接口
 * @date 2023-04-06
 */
public interface UserCollectMapper extends BaseMapperPlus<UserCollectMapper, UserCollect, UserCollectVo> {

    List<UserCollectVo> getCollectAll(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("size") Integer size);

    Long getCollectAllByCount(@Param("userId") Long userId);
}
