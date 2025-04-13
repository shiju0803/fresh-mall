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
import com.zzq.product.domain.bo.StoreProductRuleBo;
import com.zzq.product.domain.vo.StoreProductRuleVo;
import com.zzq.web.controller.admin.product.service.IStoreProductRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商品规格
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProductRule")
public class StoreProductRuleController extends BaseController {

    private final IStoreProductRuleService iWmStoreProductRuleService;

    /**
     * 查询商品规格列表
     */
    @SaCheckPermission("product:storeProductRule:list")
    @GetMapping("/list")
    public TableDataInfo<StoreProductRuleVo> list(StoreProductRuleBo bo, PageQuery pageQuery) {
        return iWmStoreProductRuleService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品规格列表
     */
    @SaCheckPermission("product:storeProductRule:export")
    @Log(title = "商品规格", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreProductRuleBo bo, HttpServletResponse response) {
        List<StoreProductRuleVo> list = iWmStoreProductRuleService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品规格", StoreProductRuleVo.class, response);
    }

    /**
     * 获取商品规格详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProductRule:query")
    @GetMapping("/{id}")
    public R<StoreProductRuleVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreProductRuleService.queryById(id));
    }

    /**
     * 新增商品规格
     */
    @SaCheckPermission("product:storeProductRule:add")
    @Log(title = "商品规格", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreProductRuleBo bo) {
        return toAjax(iWmStoreProductRuleService.insertByBo(bo));
    }

    /**
     * 修改商品规格
     */
    @SaCheckPermission("product:storeProductRule:edit")
    @Log(title = "商品规格", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreProductRuleBo bo) {
        return toAjax(iWmStoreProductRuleService.updateByBo(bo));
    }

    /**
     * 删除商品规格
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProductRule:remove")
    @Log(title = "商品规格", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductRuleService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
