package com.zzq.web.controller.admin.product;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollUtil;
import com.zzq.common.annotation.Log;
import com.zzq.common.annotation.RepeatSubmit;
import com.zzq.common.core.controller.BaseController;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.core.validate.AddGroup;
import com.zzq.common.core.validate.EditGroup;
import com.zzq.common.enums.BusinessType;
import com.zzq.common.exception.ServiceException;
import com.zzq.common.utils.poi.ExcelUtil;
import com.zzq.product.domain.bo.StoreCategoryBo;
import com.zzq.product.domain.vo.StoreCategoryVo;
import com.zzq.web.controller.admin.product.service.IStoreCategoryService;
import com.zzq.web.controller.admin.product.service.IStoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品分类
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeCategory")
public class StoreCategoryController extends BaseController {

    private final IStoreCategoryService iWmStoreCategoryService;

    private final IStoreProductService iWmStoreProductService;

    /**
     * 查询商品分类列表
     */
    @SaCheckPermission("product:storeCategory:list")
    @GetMapping("/list")
    public TableDataInfo<StoreCategoryVo> list(StoreCategoryBo bo, PageQuery pageQuery) {
        return iWmStoreCategoryService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询商品分类列表(树形)
     */
    @SaCheckPermission("product:storeCategory:list")
    @GetMapping("/listTree")
    public R<Map<String,Object>> list(StoreCategoryBo bo) {
        List<StoreCategoryVo> storeCategoryVos = iWmStoreCategoryService.queryList(bo);
        Map<String,Object> result = iWmStoreCategoryService.buildTree(storeCategoryVos);
        return R.ok(result);
    }

    /**
     * 查询商品分类列表(树形)
     */
    @SaCheckPermission("product:storeCategory:list")
    @GetMapping("/pageTree")
    public R<Map<String,Object>> pageTree(StoreCategoryBo bo, PageQuery pageQuery) {
        bo.setPid(0L);
        TableDataInfo<StoreCategoryVo> queryPageList = iWmStoreCategoryService.queryPageList(bo, pageQuery);
        List<StoreCategoryVo> rows = queryPageList.getRows();
        if (CollUtil.isNotEmpty(rows)) {
            List<Long> pids = rows.stream().map(StoreCategoryVo::getId).collect(Collectors.toList());
            StoreCategoryBo bo1 = new StoreCategoryBo();
            bo1.setPids(pids);
            List<StoreCategoryVo> storeCategoryVos1 = iWmStoreCategoryService.queryList(bo1);
            rows.addAll(storeCategoryVos1);
        }
        List<StoreCategoryVo> result = iWmStoreCategoryService.buildPageTree(rows);
        Map<String, Object> map = new HashMap<>(2);
        map.put("totalElements", queryPageList.getTotal());
        map.put("content", CollectionUtils.isEmpty(result) ? queryPageList : result);
        return R.ok(map);
    }

    /**
     * 导出商品分类列表
     */
    @SaCheckPermission("product:storeCategory:export")
    @Log(title = "商品分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreCategoryBo bo, HttpServletResponse response) {
        List<StoreCategoryVo> list = iWmStoreCategoryService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品分类", StoreCategoryVo.class, response);
    }

    /**
     * 获取商品分类详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeCategory:query")
    @GetMapping("/{id}")
    public R<StoreCategoryVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iWmStoreCategoryService.queryById(id));
    }

    /**
     * 新增商品分类
     */
    @SaCheckPermission("product:storeCategory:add")
    @Log(title = "商品分类", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreCategoryBo bo) {
//        if(bo.getPid() != null && bo.getPid() > 0 && StrUtil.isBlank(bo.getPic())) {
//            throw new ServiceException("子分类图片必传");
//        }

        boolean checkResult = iWmStoreCategoryService.checkCategory(bo.getPid());
        if(!checkResult) {
            throw new ServiceException("分类最多能添加2级哦");
        }
        return toAjax(iWmStoreCategoryService.insertByBo(bo));
    }

    /**
     * 修改商品分类
     */
    @SaCheckPermission("product:storeCategory:edit")
    @Log(title = "商品分类", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreCategoryBo bo) {

//        if(bo.getPid() != null && bo.getPid() > 0 && StrUtil.isBlank(bo.getPic())) {
//            throw new ServiceException("子分类图片必传");
//        }

        if(bo.getId().equals(bo.getPid())){
            throw new ServiceException("自己不能选择自己哦");
        }

        boolean checkResult = iWmStoreCategoryService.checkCategory(bo.getPid());

        if(!checkResult) {
            throw new ServiceException("分类最多能添加2级哦");
        }

        return toAjax(iWmStoreCategoryService.updateByBo(bo));
    }

    /**
     * 删除商品分类
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeCategory:remove")
    @Log(title = "商品分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        for (Long newId: ids) {
            this.delCheck(newId);
        }
        return toAjax(iWmStoreCategoryService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 检测删除分类
     * @param id 分类id
     */
    private void delCheck(Long id){
        Long count = iWmStoreCategoryService.selectCountByPid(id);
        if(count > 0) {
            throw new ServiceException("请先删除子分类");
        }

        Long countP = iWmStoreProductService.selectCountByCateId(id);

        if(countP > 0) {
            throw new ServiceException("当前分类下有商品不可删除");
        }
    }

}
