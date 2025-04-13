package com.zzq.web.controller.admin.activity;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzq.activity.domain.StoreActivity;
import com.zzq.activity.domain.StoreActivityProduct;
import com.zzq.activity.domain.bo.StoreActivityProductBo;
import com.zzq.activity.domain.vo.StoreActivityProductVo;
import com.zzq.activity.mapper.StoreActivityMapper;
import com.zzq.activity.mapper.StoreActivityProductMapper;
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
import com.zzq.web.controller.admin.activity.service.IStoreActivityProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/activity/storeActivityProduct")
public class StoreActivityProductController extends BaseController {

    private final IStoreActivityProductService iStoreActivityProductService;

    @Autowired
    StoreActivityMapper kxStoreActivityMapper;

    @Autowired
    StoreActivityProductMapper kxStoreActivityProductMapper;


    /**
     * 更新当前价格
     */
    @PostMapping("/updatePrice")
    public R<Boolean> updatePrice(@RequestBody StoreActivityProductBo bo){
        return R.ok(iStoreActivityProductService.updatePrice(bo));
    }


    /**
     * 获取推荐类型信息
     */
    @GetMapping("/listActivityType")
    public R<List<StoreActivity>> listActivityType() {
        List<StoreActivity> kxStoreActivities = kxStoreActivityMapper.selectList();
        return R.ok(kxStoreActivities);

    }

    /**
     * 批量新增推荐管理
     */
    @PostMapping("/addProductBatch")
    public R<Void> addProductBatch(@Validated(AddGroup.class) @RequestBody StoreActivityProductBo bo) {
        return toAjax(iStoreActivityProductService.addProductBatch(bo));
    }







    /**
     * 查询活动商品列表
     */
    @SaCheckPermission("activity:storeActivityProduct:list")
    @GetMapping("/list")
    public TableDataInfo<StoreActivityProductVo> list(StoreActivityProductBo bo, PageQuery pageQuery) {

        QueryWrapper<StoreActivityProduct> wrapper = Wrappers.query();
        wrapper.eq(bo.getActivityId() != null,"sap.activity_id", bo.getActivityId());
        Page<StoreActivityProductVo> kxStoreActivityProductVoPage = kxStoreActivityProductMapper.selectVoPageBySQL(pageQuery.build(), wrapper);
        return TableDataInfo.build(kxStoreActivityProductVoPage);
    }

    /**
     * 导出活动商品列表
     */
    @SaCheckPermission("activity:storeActivityProduct:export")
    @Log(title = "活动商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreActivityProductBo bo, HttpServletResponse response) {
        List<StoreActivityProductVo> list = iStoreActivityProductService.queryList(bo);
        ExcelUtil.exportExcel(list, "活动商品", StoreActivityProductVo.class, response);
    }

    /**
     * 获取活动商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("activity:storeActivityProduct:query")
    @GetMapping("/{id}")
    public R<StoreActivityProductVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iStoreActivityProductService.queryById(id));
    }

    /**
     * 新增活动商品
     */
    @SaCheckPermission("activity:storeActivityProduct:add")
    @Log(title = "活动商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreActivityProductBo bo) {
        return toAjax(iStoreActivityProductService.insertByBo(bo));
    }

    /**
     * 修改活动商品
     */
    @SaCheckPermission("activity:storeActivityProduct:edit")
    @Log(title = "活动商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreActivityProductBo bo) {
        return toAjax(iStoreActivityProductService.updateByBo(bo));
    }

    /**
     * 删除活动商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("activity:storeActivityProduct:remove")
    @Log(title = "活动商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iStoreActivityProductService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
