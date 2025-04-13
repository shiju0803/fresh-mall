package com.zzq.user.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.user.domain.vo.UserBillVo;
import com.zzq.user.domain.UserBill;
import com.zzq.common.core.mapper.BaseMapperPlus;
import org.apache.ibatis.annotations.Param;

/**
 * 用户账单Mapper接口
 * @date 2023-02-14
 */
public interface UserBillMapper extends BaseMapperPlus<UserBillMapper, UserBill, UserBillVo> {


    Page<UserBillVo> selectVoPageList(@Param("page") Page<Object> build, @Param(Constants.WRAPPER) QueryWrapper<UserBill> lqw);
}
