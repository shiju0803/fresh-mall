package com.zzq.web.controller.admin.storage;

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
import com.zzq.storage.domain.bo.GoodsOutStockBo;
import com.zzq.storage.domain.vo.GoodsOutStockVo;
import com.zzq.storage.domain.vo.StorageVo;
import com.zzq.web.controller.admin.storage.service.IGoodsOutStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商品出库
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/goodsOutStock")
public class GoodsOutStockController extends BaseController {

    private final IGoodsOutStockService iGoodsOutStockService;

    /**
     * 查询商品出库列表
     */
    @SaCheckPermission("storage:goodsOutStock:list")
    @GetMapping("/list")
    public TableDataInfo<GoodsOutStockVo> list(GoodsOutStockBo bo, PageQuery pageQuery) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        return iGoodsOutStockService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品出库列表
     */
    @SaCheckPermission("storage:goodsOutStock:export")
    @Log(title = "商品出库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GoodsOutStockBo bo, HttpServletResponse response) {
        List<GoodsOutStockVo> list = iGoodsOutStockService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品出库", GoodsOutStockVo.class, response);
    }

    /**
     * 获取商品出库详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("storage:goodsOutStock:query")
    @GetMapping("/{id}")
    public R<GoodsOutStockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iGoodsOutStockService.queryById(id));
    }

    /**
     * 新增商品出库
     */
    @SaCheckPermission("storage:goodsOutStock:add")
    @Log(title = "商品出库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody GoodsOutStockBo bo) {
        return toAjax(iGoodsOutStockService.insertByBo(bo));
    }

    /**
     * 修改商品出库
     */
    @SaCheckPermission("storage:goodsOutStock:edit")
    @Log(title = "商品出库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody GoodsOutStockBo bo) {
        return toAjax(iGoodsOutStockService.updateByBo(bo));
    }

    /**
     * 删除商品出库
     *
     * @param ids 主键串
     */
    @SaCheckPermission("storage:goodsOutStock:remove")
    @Log(title = "商品出库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iGoodsOutStockService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 入库
     */
    @RepeatSubmit()
    @PostMapping("/updateOutOfStock")
    public R<Boolean> updateOutOfStock(@Validated(AddGroup.class) @RequestBody GoodsOutStockBo bo) {
        return R.ok(iGoodsOutStockService.updateOutOfStock(bo));
    }


    /**
     * 获取所有仓库的名称
     */
    @RepeatSubmit()
    @PostMapping("/storagAllName")
    public R<List<StorageVo>> storagAllName(@Validated(AddGroup.class) @RequestBody GoodsOutStockBo bo) {
        return R.ok(iGoodsOutStockService.storagAllName(bo));
    }
}
