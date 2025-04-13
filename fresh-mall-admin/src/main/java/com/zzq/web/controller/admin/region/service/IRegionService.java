package com.zzq.web.controller.admin.region.service;

import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.region.domain.bo.RegionBo;
import com.zzq.region.domain.vo.RegionVo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 中国地区信息Service接口
 */
public interface IRegionService {

    /**
     * 查询中国地区信息
     */
    RegionVo queryById(Long id);

    /**
     * 查询中国地区信息列表
     */
    TableDataInfo<RegionVo> queryPageList(RegionBo bo, PageQuery pageQuery);

    /**
     * 查询中国地区信息列表
     */
    List<RegionVo> queryList(RegionBo bo);

    /**
     * 新增中国地区信息
     */
    Boolean insertByBo(RegionBo bo);

    /**
     * 修改中国地区信息
     */
    Boolean updateByBo(RegionBo bo);

    /**
     * 校验并批量删除中国地区信息信息
     */
    Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid);

    /**
     * 获取所有省份
     * @return
     */
    List<Map<String, Object>> getProvinceAll();

    List<Map<String, Object>> getCityAll();

    List<Map<String, Object>> getCountyAll();

    List<Map<String, Object>> getCity(Long provinceId);

    List<Map<String, Object>> getCounty(Long cityId);
}
