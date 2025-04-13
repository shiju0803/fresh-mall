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
import com.zzq.storage.domain.bo.StorageBo;
import com.zzq.storage.domain.vo.StorageVo;
import com.zzq.web.controller.admin.storage.service.IStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 仓库管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/storage/storage")
public class StorageController extends BaseController {

    private final IStorageService iStorageService;

    /**
     * 查询仓库管理列表
     */
    @SaCheckPermission("storage:storage:list")
    @GetMapping("/list")
    public TableDataInfo<StorageVo> list(StorageBo bo, PageQuery pageQuery) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        return iStorageService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询仓库管理列表（全部）
     */
    @GetMapping("/listAll")
    public R<List<StorageVo>> listAll(StorageBo bo) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        return R.ok(iStorageService.queryList(bo));
    }

    /**
     * 导出仓库管理列表
     */
    @SaCheckPermission("storage:storage:export")
    @Log(title = "仓库管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StorageBo bo, HttpServletResponse response) {
        bo.setStorageIds(getLoginUser().getStoragePermission());
        List<StorageVo> list = iStorageService.queryList(bo);
        ExcelUtil.exportExcel(list, "仓库管理", StorageVo.class, response);
    }

    /**
     * 获取仓库管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("storage:storage:query")
    @GetMapping("/{id}")
    public R<StorageVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iStorageService.queryById(id));
    }

    /**
     * 新增仓库管理
     */
    @SaCheckPermission("storage:storage:add")
    @Log(title = "仓库管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StorageBo bo) {
        return toAjax(iStorageService.insertByBo(bo));
    }

    /**
     * 修改仓库管理
     */
    @SaCheckPermission("storage:storage:edit")
    @Log(title = "仓库管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StorageBo bo) {
        return toAjax(iStorageService.updateByBo(bo));
    }

    /**
     * 删除仓库管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("storage:storage:remove")
    @Log(title = "仓库管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iStorageService.deleteWithValidByIds(Arrays.asList(ids), true));
    }



    /**
     * 前置仓状态批量更新为正常
     */
    @PostMapping("/updateStateToNomral")
    public R<Boolean> updateStateToNomral(@RequestBody StorageBo bo){
       return R.ok(iStorageService.updateStateToNomral(bo));
    }


    /**
     * 前置仓状态批量更新为禁用
     */
    @PostMapping("/updateStateToAbort")
    public R<Boolean> updateStateToAbort(@RequestBody StorageBo bo){
       return R.ok(iStorageService.updateStateToAbort(bo));
    }



    /**
     * 前置仓营业状态批量更新为营业中
     */
    @PostMapping("/updateBusinessStateToOpen")
    public R<Boolean> updateBusinessStateToOpen(@RequestBody StorageBo bo){
        return R.ok(iStorageService.updateBusinessStateToOpen(bo));
    }



    /**
     * 前置仓营业状态批量更新为休息中
     */
    @PostMapping("/updateBusinessStateToRest")
    public R<Boolean> updateBusinessStateToRest(@RequestBody StorageBo bo){
        return R.ok(iStorageService.updateBusinessStateToRest(bo));
    }



    /**
     * 前置仓营业状态批量更新为休息中
     */
    @PostMapping("/options")
    public R<List<StorageVo>> options(){
        return R.ok(iStorageService.options());
    }



    /**
     * 获取指定仓库的推送订阅二维码
     */
    @GetMapping("/getStorageQrcodeImage")
    public R<String> getStorageQrcodeImage(Long storageId){
        return R.ok("操作成功", iStorageService.getStorageQrcodeImage(storageId));
    }


    /**
     * 打印测试
     */
    @PostMapping("/printTest")
    public R<String> printTest(@RequestBody StorageBo bo){
        return R.ok("操作成功", iStorageService.printTest(bo));
    }






}
