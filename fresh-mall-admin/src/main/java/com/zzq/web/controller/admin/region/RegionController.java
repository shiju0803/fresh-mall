package com.zzq.web.controller.admin.region;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.zzq.common.annotation.Log;
import com.zzq.common.annotation.RepeatSubmit;
import com.zzq.common.core.controller.BaseController;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.core.validate.AddGroup;
import com.zzq.common.core.validate.EditGroup;
import com.zzq.common.enums.BusinessType;
import com.zzq.common.utils.poi.ExcelUtil;
import com.zzq.region.domain.bo.RegionBo;
import com.zzq.region.domain.vo.RegionVo;
import com.zzq.web.controller.admin.region.service.IRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 中国地区信息
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/region/region")
public class RegionController extends BaseController {

    private final IRegionService iRegionService;

    /**
     * 查询中国地区信息列表
     */
    @SaCheckPermission("region:region:list")
    @GetMapping("/list")
    public TableDataInfo<RegionVo> list(RegionBo bo, PageQuery pageQuery) {
        return iRegionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出中国地区信息列表
     */
    @SaCheckPermission("region:region:export")
    @Log(title = "中国地区信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RegionBo bo, HttpServletResponse response) {
        List<RegionVo> list = iRegionService.queryList(bo);
        ExcelUtil.exportExcel(list, "中国地区信息", RegionVo.class, response);
    }

    /**
     * 获取中国地区信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("region:region:query")
    @GetMapping("/{id}")
    public R<RegionVo> getInfo(@NotNull(message = "主键不能为空")
                                 @PathVariable Long id) {
        return R.ok(iRegionService.queryById(id));
    }

    /**
     * 新增中国地区信息
     */
    @SaCheckPermission("region:region:add")
    @Log(title = "中国地区信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RegionBo bo) {
        return toAjax(iRegionService.insertByBo(bo));
    }

    /**
     * 修改中国地区信息
     */
    @SaCheckPermission("region:region:edit")
    @Log(title = "中国地区信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RegionBo bo) {
        return toAjax(iRegionService.updateByBo(bo));
    }

    /**
     * 删除中国地区信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("region:region:remove")
    @Log(title = "中国地区信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iRegionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     *获取所有省份
     * @return
     */
    @GetMapping("/getProvinceAll")
    public List<Map<String, Object>> getProvinceAll()  {
        return iRegionService.getProvinceAll();
    }

    /**
     *获取所有城市
     * @return
     */
    @GetMapping("/getCityAll")
    public List<Map<String, Object>> getCityAll()  {
        return iRegionService.getCityAll();
    }

    /**
     *获取所有区(县)
     * @return
     */
    @GetMapping("/getCountyAll")
    public List<Map<String, Object>> getCountyAll()  {
        return iRegionService.getCountyAll();
    }


    /**
     *根据省份主键获取城市
     * @return
     */
    @GetMapping("/getCity")
    public List<Map<String, Object>> getCity(Long provinceId)  {
        return iRegionService.getCity(provinceId);
    }

    /**
     *根据城市主键获取区(县)
     * @return
     */
    @GetMapping("/getCounty")
    public List<Map<String, Object>> getCounty(Long cityId)  {
        return iRegionService.getCounty(cityId);
    }



}
