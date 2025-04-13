package com.zzq.web.controller.admin.product;

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
import com.zzq.product.domain.bo.StoreProductAttrResultBo;
import com.zzq.product.domain.vo.StoreProductAttrResultVo;
import com.zzq.web.controller.admin.product.service.IStoreProductAttrResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商品属性详情
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProductAttrResult")
public class StoreProductAttrResultController extends BaseController {

    private final IStoreProductAttrResultService iWmStoreProductAttrResultService;

    /**
     * 查询商品属性详情列表
     */
    @SaCheckPermission("product:storeProductAttrResult:list")
    @GetMapping("/list")
    public TableDataInfo<StoreProductAttrResultVo> list(StoreProductAttrResultBo bo, PageQuery pageQuery) {
        return iWmStoreProductAttrResultService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品属性详情列表
     */
    @SaCheckPermission("product:storeProductAttrResult:export")
    @Log(title = "商品属性详情", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreProductAttrResultBo bo, HttpServletResponse response) {
        List<StoreProductAttrResultVo> list = iWmStoreProductAttrResultService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品属性详情", StoreProductAttrResultVo.class, response);
    }

    /**
     * 获取商品属性详情详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProductAttrResult:query")
    @GetMapping("/{id}")
    public R<StoreProductAttrResultVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreProductAttrResultService.queryById(id));
    }

    /**
     * 新增商品属性详情
     */
    @SaCheckPermission("product:storeProductAttrResult:add")
    @Log(title = "商品属性详情", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreProductAttrResultBo bo) {
        return toAjax(iWmStoreProductAttrResultService.insertByBo(bo));
    }

    /**
     * 修改商品属性详情
     */
    @SaCheckPermission("product:storeProductAttrResult:edit")
    @Log(title = "商品属性详情", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreProductAttrResultBo bo) {
        return toAjax(iWmStoreProductAttrResultService.updateByBo(bo));
    }

    /**
     * 删除商品属性详情
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProductAttrResult:remove")
    @Log(title = "商品属性详情", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductAttrResultService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
