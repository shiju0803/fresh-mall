package com.zzq.web.controller.admin.order;

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
import com.zzq.order.domain.bo.StoreAppraiseBo;
import com.zzq.order.domain.vo.StoreAppraiseVo;
import com.zzq.web.controller.admin.order.service.IStoreAppraiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 评论管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/order/storeAppraise")
public class StoreAppraiseController extends BaseController {

    private final IStoreAppraiseService iStoreAppraiseService;

    /**
     * 查询评论管理列表
     */
    @SaCheckPermission("order:storeAppraise:list")
    @GetMapping("/list")
    public TableDataInfo<StoreAppraiseVo> list(StoreAppraiseBo bo, PageQuery pageQuery) {
        return iStoreAppraiseService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出评论管理列表
     */
    @SaCheckPermission("order:storeAppraise:export")
    @Log(title = "评论管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreAppraiseBo bo, HttpServletResponse response) {
        List<StoreAppraiseVo> list = iStoreAppraiseService.queryList(bo);
        ExcelUtil.exportExcel(list, "评论管理", StoreAppraiseVo.class, response);
    }

    /**
     * 获取评论管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("order:storeAppraise:query")
    @GetMapping("/{id}")
    public R<StoreAppraiseVo> getInfo(@NotNull(message = "主键不能为空") @PathVariable Long id) {
        return R.ok(iStoreAppraiseService.queryById(id));
    }

    /**
     * 新增评论管理
     */
    @SaCheckPermission("order:storeAppraise:add")
    @Log(title = "评论管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreAppraiseBo bo) {
        return toAjax(iStoreAppraiseService.insertByBo(bo));
    }

    /**
     * 修改评论管理
     */
    @SaCheckPermission("order:storeAppraise:edit")
    @Log(title = "评论管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreAppraiseBo bo) {
        return toAjax(iStoreAppraiseService.updateByBo(bo));
    }

    /**
     * 删除评论管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("order:storeAppraise:remove")
    @Log(title = "评论管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空") @PathVariable Long[] ids) {
        return toAjax(iStoreAppraiseService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 通过评论管理
     *
     * @param id 主键串
     */
    @GetMapping("/changeState/{id}")
    public R<Void> changeState(@PathVariable Long id) {
        return toAjax(iStoreAppraiseService.changeState(id));
    }
}
