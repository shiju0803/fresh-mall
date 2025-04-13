package com.zzq.web.controller.admin.user;

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
import com.zzq.user.domain.bo.UserLevelSettingBo;
import com.zzq.user.domain.vo.UserLevelSettingVo;
import com.zzq.web.controller.admin.user.service.IUserLevelSettingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 设置用户等级
 * @date 2023-02-21
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userLevelSetting")
public class UserLevelSettingController extends BaseController {

    private final IUserLevelSettingService iWmUserLevelSettingService;

    /**
     * 查询设置用户等级列表
     */
    @SaCheckPermission("user:userLevelSetting:list")
    @GetMapping("/list")
    public TableDataInfo<UserLevelSettingVo> list(UserLevelSettingBo bo, PageQuery pageQuery) {
        return iWmUserLevelSettingService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出设置用户等级列表
     */
    @SaCheckPermission("user:userLevelSetting:export")
    @Log(title = "设置用户等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(UserLevelSettingBo bo, HttpServletResponse response) {
        List<UserLevelSettingVo> list = iWmUserLevelSettingService.queryList(bo);
        ExcelUtil.exportExcel(list, "设置用户等级", UserLevelSettingVo.class, response);
    }

    /**
     * 获取设置用户等级详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("user:userLevelSetting:query")
    @GetMapping("/{id}")
    public R<UserLevelSettingVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmUserLevelSettingService.queryById(id));
    }

    /**
     * 新增设置用户等级
     */
    @SaCheckPermission("user:userLevelSetting:add")
    @Log(title = "设置用户等级", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserLevelSettingBo bo) {
        return toAjax(iWmUserLevelSettingService.insertByBo(bo));
    }

    /**
     * 修改设置用户等级
     */
    @SaCheckPermission("user:userLevelSetting:edit")
    @Log(title = "设置用户等级", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserLevelSettingBo bo) {
        return toAjax(iWmUserLevelSettingService.updateByBo(bo));
    }

    /**
     * 删除设置用户等级
     *
     * @param ids 主键串
     */
    @SaCheckPermission("user:userLevelSetting:remove")
    @Log(title = "设置用户等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmUserLevelSettingService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
