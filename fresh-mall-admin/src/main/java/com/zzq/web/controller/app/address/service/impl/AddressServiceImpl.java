package com.zzq.web.controller.app.address.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.address.domain.Address;
import com.zzq.address.domain.bo.AddressBo;
import com.zzq.address.domain.vo.AddressVo;
import com.zzq.address.mapper.AddressMapper;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.enums.DefaultAddressStatusType;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.StringUtils;
import com.zzq.web.controller.app.address.service.IAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户下单地址Service业务层处理
 * @date 2023-04-06
 */
@RequiredArgsConstructor
@Service
public class AddressServiceImpl implements IAddressService {

    private final AddressMapper baseMapper;

    /**
     * 查询用户下单地址
     */
    @Override
    public AddressVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询用户下单地址列表
     */
    @Override
    public TableDataInfo<AddressVo> queryPageList(AddressBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Address> lqw = buildQueryWrapper(bo);
        Page<AddressVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户下单地址列表
     */
    @Override
    public List<AddressVo> queryList(AddressBo bo) {
        LambdaQueryWrapper<Address> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Address> buildQueryWrapper(AddressBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<Address> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getProvince()), Address::getProvince, bo.getProvince());
        lqw.eq(StringUtils.isNotBlank(bo.getCity()), Address::getCity, bo.getCity());
        lqw.eq(StringUtils.isNotBlank(bo.getCounty()), Address::getCounty, bo.getCounty());
        lqw.eq(StringUtils.isNotBlank(bo.getAddress()), Address::getAddress, bo.getAddress());
        lqw.eq(bo.getDefaultAddress() != null, Address::getDefaultAddress, bo.getDefaultAddress());
        lqw.eq(bo.getUserId() != null, Address::getUserId, bo.getUserId());
        lqw.eq(StringUtils.isNotBlank(bo.getPhone()), Address::getPhone, bo.getPhone());
        lqw.eq(StringUtils.isNotBlank(bo.getConsignee()), Address::getConsignee, bo.getConsignee());
        lqw.eq(bo.getLongitude() != null, Address::getLongitude, bo.getLongitude());
        lqw.eq(bo.getLatitude() != null, Address::getLatitude, bo.getLatitude());
        return lqw;
    }

    /**
     * 新增用户下单地址
     */
    @Override
    public Boolean insertByBo(AddressBo bo) {
        Long addressNum = baseMapper.selectCount(new QueryWrapper<Address>().eq("user_id", bo.getUserId()));
        Address address = null;
        if (addressNum == 0) {
            address = BeanUtil.toBean(bo, Address.class);
            address.setDefaultAddress(DefaultAddressStatusType.DEFAULT_ADDRESS.getCode());
        } else {
            if (bo.getDefaultAddress() != 0) {
                Address preDefault = new Address();
                preDefault.setDefaultAddress(0);
                if (!(baseMapper.update(preDefault //该用户有地址却没有默认地址，抛出该异常
                    , new QueryWrapper<Address>()
                        .eq("user_id", bo.getUserId())
                        .eq("default_address", DefaultAddressStatusType.DEFAULT_ADDRESS.getCode())) > 0)) {
                    throw new ServiceException("地址更新失败！");
                }
                address = BeanUtil.toBean(bo, Address.class);
                address.setDefaultAddress(DefaultAddressStatusType.DEFAULT_ADDRESS.getCode());
            } else {
                address = BeanUtil.toBean(bo, Address.class);
                address.setDefaultAddress(DefaultAddressStatusType.COMMON_ADDRESS.getCode());
            }
        }
        Date now = new Date();
        address.setCreateTime(now);
        address.setUpdateTime(now);
        if (baseMapper.insert(address) > 0) {
            return true;
        } else {
            throw new ServiceException("地址更新失败！");
        }
    }

    /**
     * 修改用户下单地址
     */
    @Override
    public Boolean updateByBo(AddressBo bo) {
        Address update = BeanUtil.toBean(bo, Address.class);
        validEntityBeforeSave(update);
        Date now = new Date();
        if (bo.getDefaultAddress() != 0L) {
            bo.setDefaultAddress(DefaultAddressStatusType.DEFAULT_ADDRESS.getCode());
            List<Address> addresses = baseMapper.selectList(new QueryWrapper<Address>().eq("user_id", bo.getUserId()).eq("default_address", bo.getDefaultAddress()));
            if (addresses != null && addresses.size() != 0) {
                Address preDefault = addresses.get(0);
                preDefault.setDefaultAddress(0);
                baseMapper.updateById(preDefault);
            }
        }
        update.setUpdateTime(now);
        return baseMapper.update(update, new QueryWrapper<Address>()
            .eq("id", bo.getId())
            .eq("user_id", bo.getUserId())) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Address entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户下单地址
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public AddressVo getDefAddress(Long userId) {
       return baseMapper.selectVoOne(new LambdaQueryWrapper<Address>()
            .eq(Address::getUserId, userId)
            .eq(Address::getDefaultAddress, 1));
    }

    @Override
    public Boolean deleteAddress(AddressBo bo) {
        return baseMapper.deleteById(bo.getId()) > 0;
    }
}
