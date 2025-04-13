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
import com.zzq.order.domain.bo.StoreCartBo;
import com.zzq.order.domain.vo.StoreCartVo;
import com.zzq.web.controller.admin.order.service.IStoreCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 购物车
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/order/storeCart")
public class StoreCartController extends BaseController {

    private final IStoreCartService kxStoreCartService;

    /**
     * 查询购物车列表
     */
    @SaCheckPermission("order:storeCart:list")
    @GetMapping("/list")
    public TableDataInfo<StoreCartVo> list(StoreCartBo bo, PageQuery pageQuery) {
        return kxStoreCartService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出购物车列表
     */
    @SaCheckPermission("order:storeCart:export")
    @Log(title = "购物车", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreCartBo bo, HttpServletResponse response) {
        List<StoreCartVo> list = kxStoreCartService.queryList(bo);
        ExcelUtil.exportExcel(list, "购物车", StoreCartVo.class, response);
    }

    /**
     * 获取购物车详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("order:storeCart:query")
    @GetMapping("/{id}")
    public R<StoreCartVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(kxStoreCartService.queryById(id));
    }

    /**
     * 新增购物车
     */
    @SaCheckPermission("order:storeCart:add")
    @Log(title = "购物车", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreCartBo bo) {
        return toAjax(kxStoreCartService.insertByBo(bo));
    }

    /**
     * 修改购物车
     */
    @SaCheckPermission("order:storeCart:edit")
    @Log(title = "购物车", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreCartBo bo) {
        return toAjax(kxStoreCartService.updateByBo(bo));
    }

    /**
     * 删除购物车
     *
     * @param ids 主键串
     */
    @SaCheckPermission("order:storeCart:remove")
    @Log(title = "购物车", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(kxStoreCartService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
