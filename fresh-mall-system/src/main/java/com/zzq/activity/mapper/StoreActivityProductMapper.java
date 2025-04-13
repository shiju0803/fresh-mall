package com.zzq.activity.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.activity.domain.StoreActivityProduct;
import com.zzq.activity.domain.vo.StoreActivityProductVo;
import com.zzq.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 活动商品Mapper接口
 */
public interface StoreActivityProductMapper extends BaseMapperPlus<StoreActivityProductMapper, StoreActivityProduct, StoreActivityProductVo> {

    Page<StoreActivityProductVo> selectVoPageBySQL(@Param("page") Page<StoreActivityProduct> page, @Param(Constants.WRAPPER) Wrapper<StoreActivityProduct> lqw);

    List<StoreActivityProductVo> getActivityProductByStorage(@Param("storageId") Long storageId, @Param("activityId")  String activityId, @Param("offset")  Integer offset, @Param("size") Integer size);

    Long getActivityProductByStorageCount(@Param("storageId") Long storageId,@Param("activityId")  String activityId);
}
