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
import com.zzq.product.domain.bo.StoreProductAttrValueBo;
import com.zzq.product.domain.vo.StoreProductAttrValueVo;
import com.zzq.web.controller.admin.product.service.IStoreProductAttrValueService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商品属性值
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProductAttrValue")
public class StoreProductAttrValueController extends BaseController {

    private final IStoreProductAttrValueService iWmStoreProductAttrValueService;

    /**
     * 查询商品属性值列表
     */
    @SaCheckPermission("product:storeProductAttrValue:list")
    @GetMapping("/list")
    public TableDataInfo<StoreProductAttrValueVo> list(StoreProductAttrValueBo bo, PageQuery pageQuery) {
        return iWmStoreProductAttrValueService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品属性值列表
     */
    @SaCheckPermission("product:storeProductAttrValue:export")
    @Log(title = "商品属性值", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreProductAttrValueBo bo, HttpServletResponse response) {
        List<StoreProductAttrValueVo> list = iWmStoreProductAttrValueService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品属性值", StoreProductAttrValueVo.class, response);
    }

    /**
     * 获取商品属性值详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProductAttrValue:query")
    @GetMapping("/{id}")
    public R<StoreProductAttrValueVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreProductAttrValueService.queryById(id));
    }

    /**
     * 新增商品属性值
     */
    @SaCheckPermission("product:storeProductAttrValue:add")
    @Log(title = "商品属性值", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreProductAttrValueBo bo) {
        return toAjax(iWmStoreProductAttrValueService.insertByBo(bo));
    }

    /**
     * 修改商品属性值
     */
    @SaCheckPermission("product:storeProductAttrValue:edit")
    @Log(title = "商品属性值", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreProductAttrValueBo bo) {
        return toAjax(iWmStoreProductAttrValueService.updateByBo(bo));
    }

    /**
     * 删除商品属性值
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProductAttrValue:remove")
    @Log(title = "商品属性值", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductAttrValueService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
