package com.zzq.web.controller.app.address.service;

import com.zzq.address.domain.bo.AddressBo;
import com.zzq.address.domain.vo.AddressVo;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;

import java.util.Collection;
import java.util.List;

/**
 * 用户下单地址Service接口
 * @date 2023-04-06
 */
public interface IAddressService {

    /**
     * 查询用户下单地址
     */
    AddressVo queryById(Long id);

    /**
     * 查询用户下单地址列表
     */
    TableDataInfo<AddressVo> queryPageList(AddressBo bo, PageQuery pageQuery);

    /**
     * 查询用户下单地址列表
     */
    List<AddressVo> queryList(AddressBo bo);

    /**
     * 新增用户下单地址
     */
    Boolean insertByBo(AddressBo bo);

    /**
     * 修改用户下单地址
     */
    Boolean updateByBo(AddressBo bo);

    /**
     * 校验并批量删除用户下单地址信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取默认地址
     * @param userId
     * @return
     */
    AddressVo getDefAddress(Long userId);

    Boolean deleteAddress(AddressBo bo);
}
