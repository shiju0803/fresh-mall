package com.zzq.web.controller.admin.system;

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
import com.zzq.system.domain.bo.SysRegionBo;
import com.zzq.system.domain.vo.SysRegionVo;
import com.zzq.web.controller.admin.system.service.ISysRegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 中国地区系统
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/region")
public class SysRegionController extends BaseController {

    private final ISysRegionService iSysRegionService;

    /**
     * 查询中国地区系统列表
     */
    @SaCheckPermission("system:region:list")
    @GetMapping("/list")
    public TableDataInfo<SysRegionVo> list(SysRegionBo bo, PageQuery pageQuery) {
        return iSysRegionService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出中国地区系统列表
     */
    @SaCheckPermission("system:region:export")
    @Log(title = "中国地区系统", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(SysRegionBo bo, HttpServletResponse response) {
        List<SysRegionVo> list = iSysRegionService.queryList(bo);
        ExcelUtil.exportExcel(list, "中国地区系统", SysRegionVo.class, response);
    }

    /**
     * 获取中国地区系统详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:region:query")
    @GetMapping("/{id}")
    public R<SysRegionVo> getInfo(@NotNull(message = "主键不能为空")
                                  @PathVariable Long id) {
        return R.ok(iSysRegionService.queryById(id));
    }

    /**
     * 新增中国地区系统
     */
    @SaCheckPermission("system:region:add")
    @Log(title = "中国地区系统", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody SysRegionBo bo) {
        return toAjax(iSysRegionService.insertByBo(bo));
    }

    /**
     * 修改中国地区系统
     */
    @SaCheckPermission("system:region:edit")
    @Log(title = "中国地区系统", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody SysRegionBo bo) {
        return toAjax(iSysRegionService.updateByBo(bo));
    }

    /**
     * 删除中国地区系统
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:region:remove")
    @Log(title = "中国地区系统", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iSysRegionService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
