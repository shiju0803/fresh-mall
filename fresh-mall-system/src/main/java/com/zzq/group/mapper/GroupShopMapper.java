package com.zzq.group.mapper;

import com.zzq.common.core.mapper.BaseMapperPlus;
import com.zzq.group.domain.GroupShop;
import com.zzq.group.domain.vo.GroupShopVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 团购Mapper接口
 * @date 2023-10-07
 */
public interface GroupShopMapper extends BaseMapperPlus<GroupShopMapper, GroupShop, GroupShopVo> {


    List<GroupShopVo> getGroupShopPage(@Param("storageId") Long storageId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    GroupShopVo detail(@Param("groupShopId") Long groupShopId, @Param("storageId") Long storageId);

    Integer incCurrentNum(@Param("id") Long id,@Param("num") Integer num);
}
