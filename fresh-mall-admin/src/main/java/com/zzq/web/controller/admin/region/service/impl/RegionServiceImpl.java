package com.zzq.web.controller.admin.region.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.region.domain.Region;
import com.zzq.region.domain.bo.RegionBo;
import com.zzq.region.domain.vo.RegionVo;
import com.zzq.region.mapper.RegionMapper;
import com.zzq.web.controller.admin.region.service.IRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 中国地区信息Service业务层处理
 */
@RequiredArgsConstructor
@Service
public class RegionServiceImpl implements IRegionService {

    private final RegionMapper baseMapper;


    private static final Integer PROVINCE_LEVEL = 1;
    private static final Integer CITY_LEVEL = 2;
    private static final Integer COUNTY_LEVEL = 3;
    private static final String PROVINCE_INFO_DATA = "PROVINCE_INFO_DATA";
    private static final String CITY_INFO_DATA = "CITY_INFO_DATA";
    private static final String COUNTY_INFO_DATA = "COUNTY_INFO_DATA";
    private static final String CITY_INFO_DATA_PREFIX = "CITY_INFO_DATA_PREFIX_";
    private static final String COUNTY_INFO_DATA_PREFIX = "COUNTY_INFO_DATA_PREFIX_";

    /**
     * 查询中国地区信息
     */
    @Override
    public RegionVo queryById(Long id) {
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询中国地区信息列表
     */
    @Override
    public TableDataInfo<RegionVo> queryPageList(RegionBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<Region> lqw = buildQueryWrapper(bo);
        Page<RegionVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询中国地区信息列表
     */
    @Override
    public List<RegionVo> queryList(RegionBo bo) {
        LambdaQueryWrapper<Region> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<Region> buildQueryWrapper(RegionBo bo) {
        LambdaQueryWrapper<Region> lqw = Wrappers.lambdaQuery();
        lqw.eq(StringUtils.isNotBlank(bo.getCode()), Region::getCode, bo.getCode());
        lqw.like(StringUtils.isNotBlank(bo.getName()), Region::getName, bo.getName());
        lqw.like(StringUtils.isNotBlank(bo.getShortName()), Region::getShortName, bo.getShortName());
        lqw.eq(StringUtils.isNotBlank(bo.getSuperiorCode()), Region::getSuperiorCode, bo.getSuperiorCode());
        lqw.eq(StringUtils.isNotBlank(bo.getLng()), Region::getLng, bo.getLng());
        lqw.eq(StringUtils.isNotBlank(bo.getLat()), Region::getLat, bo.getLat());
        lqw.eq(bo.getSort() != null, Region::getSort, bo.getSort());
        lqw.eq(StringUtils.isNotBlank(bo.getRamark()), Region::getRamark, bo.getRamark());
        lqw.eq(bo.getState() != null, Region::getState, bo.getState());
        lqw.eq(StringUtils.isNotBlank(bo.getTenantCode()), Region::getTenantCode, bo.getTenantCode());
        lqw.eq(bo.getLevel() != null, Region::getLevel, bo.getLevel());
        return lqw;
    }

    /**
     * 新增中国地区信息
     */
    @Override
    public Boolean insertByBo(RegionBo bo) {
        Region add = BeanUtil.toBean(bo, Region.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改中国地区信息
     */
    @Override
    public Boolean updateByBo(RegionBo bo) {
        Region update = BeanUtil.toBean(bo, Region.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(Region entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除中国地区信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    @Override
    public List<Map<String, Object>> getProvinceAll() {
        List<Region> regionDOList = RedisUtils.getCacheList(PROVINCE_INFO_DATA);
        if (regionDOList == null || regionDOList.size() == 0) {
            LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Region::getLevel, PROVINCE_LEVEL);
            lambdaQueryWrapper.orderByAsc(Region::getSort);
            regionDOList = baseMapper.selectList(lambdaQueryWrapper);
            if (regionDOList != null && regionDOList.size() > 0) {
                RedisUtils.setCacheList(PROVINCE_INFO_DATA, regionDOList);
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政省为空!");
    }

    @Override
    public List<Map<String, Object>> getCityAll() {
        List<Region> regionDOList = RedisUtils.getCacheList(CITY_INFO_DATA);
        if (regionDOList == null || regionDOList.size() == 0) {
            LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Region::getLevel, CITY_LEVEL);
            lambdaQueryWrapper.orderByAsc(Region::getSort);
            regionDOList = baseMapper.selectList(lambdaQueryWrapper);
            if (regionDOList != null && regionDOList.size() > 0) {
                RedisUtils.setCacheList(CITY_INFO_DATA, regionDOList);
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政市为空！");
    }

    @Override
    public List<Map<String, Object>> getCountyAll() {
        List<Region> regionDOList = RedisUtils.getCacheList(COUNTY_INFO_DATA);
        if (regionDOList == null || regionDOList.size() == 0) {
            LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(Region::getLevel, COUNTY_LEVEL);
            lambdaQueryWrapper.orderByAsc(Region::getSort);
            regionDOList = baseMapper.selectList(lambdaQueryWrapper);
            if (regionDOList != null && regionDOList.size() > 0) {
                RedisUtils.setCacheList(COUNTY_INFO_DATA, regionDOList);
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政区（县）为空！");
    }

    @Override
    public List<Map<String, Object>> getCity(Long provinceId) {
        List<Region> regionDOList = RedisUtils.getCacheList(CITY_INFO_DATA_PREFIX + provinceId);
        if (regionDOList == null || regionDOList.size() == 0) {
            Region provinceRegionDO = baseMapper.selectById(provinceId);
            if (provinceRegionDO != null) {
                LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Region::getLevel, CITY_LEVEL);
                lambdaQueryWrapper.eq(Region::getSuperiorCode, provinceRegionDO.getCode());
                lambdaQueryWrapper.orderByAsc(Region::getSort);
                regionDOList = baseMapper.selectList(lambdaQueryWrapper);
                if (regionDOList != null && regionDOList.size() > 0) {
                    RedisUtils.setCacheList(CITY_INFO_DATA_PREFIX + provinceId, regionDOList);
                }
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政市为空！");
    }

    @Override
    public List<Map<String, Object>> getCounty(Long cityId) {
        List<Region> regionDOList = RedisUtils.getCacheList(COUNTY_INFO_DATA_PREFIX + cityId);
        if (regionDOList == null || regionDOList.size() == 0) {
            Region cityRegionDO = baseMapper.selectById(cityId);
            if (cityRegionDO != null) {
                LambdaQueryWrapper<Region> lambdaQueryWrapper = new LambdaQueryWrapper<>();
                lambdaQueryWrapper.eq(Region::getLevel, COUNTY_LEVEL);
                lambdaQueryWrapper.eq(Region::getSuperiorCode, cityRegionDO.getCode());
                lambdaQueryWrapper.orderByAsc(Region::getSort);
                regionDOList = baseMapper.selectList(lambdaQueryWrapper);
                if (regionDOList != null && regionDOList.size() > 0) {
                    RedisUtils.setCacheList(COUNTY_INFO_DATA_PREFIX + cityId, regionDOList);
                }
            }
        }
        List<Map<String, Object>> list = buildRegionListMapInfo(regionDOList);
        if (list != null) {
            return list;
        }
        throw new ServiceException("行政区（县）为空！");
    }

    private List<Map<String, Object>> buildRegionListMapInfo(List<Region> regionDOList) {
        if (regionDOList != null && regionDOList.size() > 0) {
            List<Map<String, Object>> list = new LinkedList<>();
            regionDOList.forEach(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("value", item.getId());
                map.put("label", item.getName());
                list.add(map);
            });
            return list;
        }
        return null;
    }
}
