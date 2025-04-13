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
import com.zzq.user.domain.bo.UserBillBo;
import com.zzq.user.domain.vo.UserBillVo;
import com.zzq.web.controller.admin.user.service.IUserBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户账单
 * @date 2023-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userBill")
public class UserBillController extends BaseController {

    private final IUserBillService iWmUserBillService;

    /**
     * 查询用户账单列表
     */
    @SaCheckPermission("user:userBill:list")
    @GetMapping("/list")
    public TableDataInfo<UserBillVo> list(UserBillBo bo, PageQuery pageQuery) {
        return iWmUserBillService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户账单列表
     */
    @SaCheckPermission("user:userBill:export")
    @Log(title = "用户账单", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(UserBillBo bo, HttpServletResponse response) {
        List<UserBillVo> list = iWmUserBillService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户账单", UserBillVo.class, response);
    }

    /**
     * 获取用户账单详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("user:userBill:query")
    @GetMapping("/{id}")
    public R<UserBillVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmUserBillService.queryById(id));
    }

    /**
     * 新增用户账单
     */
    @SaCheckPermission("user:userBill:add")
    @Log(title = "用户账单", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserBillBo bo) {
        return toAjax(iWmUserBillService.insertByBo(bo));
    }

    /**
     * 修改用户账单
     */
    @SaCheckPermission("user:userBill:edit")
    @Log(title = "用户账单", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserBillBo bo) {
        return toAjax(iWmUserBillService.updateByBo(bo));
    }

    /**
     * 删除用户账单
     *
     * @param ids 主键串
     */
    @SaCheckPermission("user:userBill:remove")
    @Log(title = "用户账单", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmUserBillService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
