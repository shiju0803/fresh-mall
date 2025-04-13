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
import com.zzq.storage.domain.bo.StockBo;
import com.zzq.storage.domain.bo.WarningStockBo;
import com.zzq.storage.domain.vo.StockVo;
import com.zzq.web.controller.admin.storage.service.IStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 前置仓商品
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/stock")
public class StockController extends BaseController {

    private final IStockService iStockService;

    /**
     * 查询前置仓商品列表
     */
    @SaCheckPermission("storage:stock:list")
    @GetMapping("/list")
    public TableDataInfo<StockVo> list(StockBo bo, PageQuery pageQuery) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        return iStockService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出前置仓商品列表
     */
    @SaCheckPermission("storage:stock:export")
    @Log(title = "前置仓商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StockBo bo, HttpServletResponse response) {
        List<StockVo> list = iStockService.queryList(bo);
        ExcelUtil.exportExcel(list, "前置仓商品", StockVo.class, response);
    }

    /**
     * 获取前置仓商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("storage:stock:query")
    @GetMapping("/{id}")
    public R<StockVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iStockService.queryById(id));
    }

    /**
     * 新增前置仓商品
     */
    @SaCheckPermission("storage:stock:add")
    @Log(title = "前置仓商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StockBo bo) {
        return toAjax(iStockService.insertByBo(bo));
    }

    /**
     * 修改前置仓商品
     */
    @SaCheckPermission("storage:stock:edit")
    @Log(title = "前置仓商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StockBo bo) {
        return toAjax(iStockService.updateByBo(bo));
    }

    /**
     * 删除前置仓商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("storage:stock:remove")
    @Log(title = "前置仓商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iStockService.deleteWithValidByIds(Arrays.asList(ids), true));
    }


    /**
     * 上下架
     */
    @PostMapping("/freezeOrActivation")
    public R<Boolean> freezeOrActivation(@RequestBody StockBo bo){
        return R.ok(iStockService.freezeOrActivation(bo));
    }


    /**
     * 逻辑删除
     */
    @PostMapping("/updateByStock")
    public R<Boolean> updateByStock(@RequestBody StockBo bo){
        return R.ok(iStockService.updateByStock(bo));
    }


    /**
     * 更新当前价格
     */
    @PostMapping("/updatePrice")
    public R<Boolean> updatePrice(@RequestBody StockBo bo){
        return R.ok(iStockService.updatePrice(bo));
    }


    /**
     * 库存预警列表
     */
    @GetMapping("/warningList")
    public TableDataInfo<StockVo> warningList(WarningStockBo bo, PageQuery pageQuery) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        return iStockService.queryPageWarningList(bo, pageQuery);
    }


    /**
     * 设置预警量
     */
    @PostMapping("/warningUpdate")
    public R<Boolean> warningUpdate(@RequestBody WarningStockBo bo) {
        return R.ok(iStockService.warningUpdate(bo));
    }



    /**
     * 仓库二维码
     */
    @PostMapping("/warehouseCode")
    public R<String> warehouseCode(@RequestBody WarningStockBo bo) {
        return R.ok(iStockService.warehouseCode(bo));
    }
}
