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
import com.zzq.user.domain.bo.UserLevelBo;
import com.zzq.user.domain.vo.UserLevelVo;
import com.zzq.web.controller.admin.user.service.IUserLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户等级
 * @date 2023-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userLevel")
public class UserLevelController extends BaseController {

    private final IUserLevelService iWmUserLevelService;

    /**
     * 查询用户等级列表
     */
    @SaCheckPermission("user:userLevel:list")
    @GetMapping("/list")
    public TableDataInfo<UserLevelVo> list(UserLevelBo bo, PageQuery pageQuery) {
        return iWmUserLevelService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户等级列表
     */
    @SaCheckPermission("user:userLevel:export")
    @Log(title = "用户等级", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(UserLevelBo bo, HttpServletResponse response) {
        List<UserLevelVo> list = iWmUserLevelService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户等级", UserLevelVo.class, response);
    }

    /**
     * 获取用户等级详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("user:userLevel:query")
    @GetMapping("/{id}")
    public R<UserLevelVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmUserLevelService.queryById(id));
    }

    /**
     * 新增用户等级
     */
    @SaCheckPermission("user:userLevel:add")
    @Log(title = "用户等级", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserLevelBo bo) {
        return toAjax(iWmUserLevelService.insertByBo(bo));
    }

    /**
     * 修改用户等级
     */
    @SaCheckPermission("user:userLevel:edit")
    @Log(title = "用户等级", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserLevelBo bo) {
        return toAjax(iWmUserLevelService.updateByBo(bo));
    }

    /**
     * 删除用户等级
     *
     * @param ids 主键串
     */
    @SaCheckPermission("user:userLevel:remove")
    @Log(title = "用户等级", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmUserLevelService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
