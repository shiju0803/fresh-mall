package com.zzq.web.controller.admin.activity;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.zzq.activity.domain.bo.StoreActivityBo;
import com.zzq.activity.domain.vo.StoreActivityVo;
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
import com.zzq.web.controller.admin.activity.service.IStoreActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 活动商品
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/activity/storeActivity")
public class StoreActivityController extends BaseController {

    private final IStoreActivityService iStoreActivityService;

    /**
     * 查询活动商品列表
     */
    @SaCheckPermission("activity:storeActivity:list")
    @GetMapping("/list")
    public TableDataInfo<StoreActivityVo> list(StoreActivityBo bo, PageQuery pageQuery) {
        return iStoreActivityService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出活动商品列表
     */
    @SaCheckPermission("activity:storeActivity:export")
    @Log(title = "活动商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreActivityBo bo, HttpServletResponse response) {
        List<StoreActivityVo> list = iStoreActivityService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动商品", StoreActivityVo.class, response);
    }

    /**
     * 获取活动商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("activity:storeActivity:query")
    @GetMapping("/{id}")
    public R<StoreActivityVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iStoreActivityService.queryById(id));
    }

    /**
     * 新增活动商品
     */
    @SaCheckPermission("activity:storeActivity:add")
    @Log(title = "活动商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreActivityBo bo) {
        return toAjax(iStoreActivityService.insertByBo(bo));
    }

    /**
     * 修改活动商品
     */
    @SaCheckPermission("activity:storeActivity:edit")
    @Log(title = "活动商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreActivityBo bo) {
        return toAjax(iStoreActivityService.updateByBo(bo));
    }

    /**
     * 删除活动商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("activity:storeActivity:remove")
    @Log(title = "活动商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iStoreActivityService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
