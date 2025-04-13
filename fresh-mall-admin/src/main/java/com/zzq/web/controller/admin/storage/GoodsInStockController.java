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
import com.zzq.storage.domain.bo.GoodsInStockBo;
import com.zzq.storage.domain.vo.GoodsInStockVo;
import com.zzq.storage.domain.vo.StorageVo;
import com.zzq.web.controller.admin.storage.service.IGoodsInStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商品入库
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/goodsInStock")
public class GoodsInStockController extends BaseController {

    private final IGoodsInStockService iGoodsInStockService;

    /**
     * 查询商品入库列表
     */
    @SaCheckPermission("storage:goodsInStock:list")
    @GetMapping("/list")
    public TableDataInfo<GoodsInStockVo> list(GoodsInStockBo bo, PageQuery pageQuery) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        return iGoodsInStockService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品入库列表
     */
    @SaCheckPermission("storage:goodsInStock:export")
    @Log(title = "商品入库", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(GoodsInStockBo bo, HttpServletResponse response) {
        List<GoodsInStockVo> list = iGoodsInStockService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品入库", GoodsInStockVo.class, response);
    }

    /**
     * 获取商品入库详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("storage:goodsInStock:query")
    @GetMapping("/{id}")
    public R<GoodsInStockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iGoodsInStockService.queryById(id));
    }

    /**
     * 新增商品入库
     */
    @SaCheckPermission("storage:goodsInStock:add")
    @Log(title = "商品入库", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody GoodsInStockBo bo) {
        return toAjax(iGoodsInStockService.insertByBo(bo));
    }

    /**
     * 修改商品入库
     */
    @SaCheckPermission("storage:goodsInStock:edit")
    @Log(title = "商品入库", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody GoodsInStockBo bo) {
        return toAjax(iGoodsInStockService.updateByBo(bo));
    }

    /**
     * 删除商品入库
     *
     * @param ids 主键串
     */
    @SaCheckPermission("storage:goodsInStock:remove")
    @Log(title = "商品入库", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iGoodsInStockService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 入库
     */
    @RepeatSubmit()
    @PostMapping("/updateInOfStock")
    public R<Boolean> updateInOfStock(@Validated(AddGroup.class) @RequestBody GoodsInStockBo bo) {
        return R.ok(iGoodsInStockService.updateInOfStock(bo));
    }

    /**
     * 获取所有仓库的名称
     */
    @RepeatSubmit()
    @PostMapping("/storagAllName")
    public R<List<StorageVo>> storagAllName(@Validated(AddGroup.class) @RequestBody GoodsInStockBo bo) {
        return R.ok(iGoodsInStockService.storagAllName(bo));
    }
}
