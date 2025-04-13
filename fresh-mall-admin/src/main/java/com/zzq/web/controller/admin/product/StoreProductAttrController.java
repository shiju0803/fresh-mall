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
import com.zzq.product.domain.bo.StoreProductAttrBo;
import com.zzq.product.domain.vo.StoreProductAttrVo;
import com.zzq.web.controller.admin.product.service.IStoreProductAttrService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商品属性
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProductAttr")
public class StoreProductAttrController extends BaseController {

    private final IStoreProductAttrService iWmStoreProductAttrService;

    /**
     * 查询商品属性列表
     */
    @SaCheckPermission("product:storeProductAttr:list")
    @GetMapping("/list")
    public TableDataInfo<StoreProductAttrVo> list(StoreProductAttrBo bo, PageQuery pageQuery) {
        return iWmStoreProductAttrService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品属性列表
     */
    @SaCheckPermission("product:storeProductAttr:export")
    @Log(title = "商品属性", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreProductAttrBo bo, HttpServletResponse response) {
        List<StoreProductAttrVo> list = iWmStoreProductAttrService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品属性", StoreProductAttrVo.class, response);
    }

    /**
     * 获取商品属性详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProductAttr:query")
    @GetMapping("/{id}")
    public R<StoreProductAttrVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreProductAttrService.queryById(id));
    }

    /**
     * 新增商品属性
     */
    @SaCheckPermission("product:storeProductAttr:add")
    @Log(title = "商品属性", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreProductAttrBo bo) {
        return toAjax(iWmStoreProductAttrService.insertByBo(bo));
    }

    /**
     * 修改商品属性
     */
    @SaCheckPermission("product:storeProductAttr:edit")
    @Log(title = "商品属性", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreProductAttrBo bo) {
        return toAjax(iWmStoreProductAttrService.updateByBo(bo));
    }

    /**
     * 删除商品属性
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProductAttr:remove")
    @Log(title = "商品属性", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductAttrService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
