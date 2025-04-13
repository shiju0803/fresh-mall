package com.zzq.web.controller.admin.product;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzq.common.annotation.Log;
import com.zzq.common.annotation.RepeatSubmit;
import com.zzq.common.constant.ShopConstants;
import com.zzq.common.core.controller.BaseController;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.common.core.validate.AddGroup;
import com.zzq.common.core.validate.EditGroup;
import com.zzq.common.enums.BusinessType;
import com.zzq.common.utils.poi.ExcelUtil;
import com.zzq.product.domain.bo.StoreProductBo;
import com.zzq.product.domain.vo.ProductTreeNodeVo;
import com.zzq.product.domain.vo.StoreProductVo;
import com.zzq.web.controller.admin.product.service.IStoreProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 商品
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/product/storeProduct")
public class StoreProductController extends BaseController {

    private final IStoreProductService iWmStoreProductService;

    /**
     * 查询商品列表
     */
    @SaCheckPermission("product:storeProduct:list")
    @GetMapping("/list")
    public TableDataInfo<StoreProductVo> list(StoreProductBo bo, PageQuery pageQuery) {
        return iWmStoreProductService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商品列表
     */
    @SaCheckPermission("product:storeProduct:export")
    @Log(title = "商品", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(StoreProductBo bo, HttpServletResponse response) {
        List<StoreProductVo> list = iWmStoreProductService.queryList(bo);
        ExcelUtil.exportExcel(list, "商品", StoreProductVo.class, response);
    }

    /**
     * 获取商品详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("product:storeProduct:query")
    @GetMapping("/{id}")
    public R<Map<String,Object>> getInfo(@NotNull(message = "主键不能为空")
                                       @PathVariable Long id) {
        return R.ok(iWmStoreProductService.queryById(id));
    }

    /**
     * 新增商品
     */
    @SaCheckPermission("product:storeProduct:add")
    @Log(title = "商品", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody StoreProductBo bo) {
        return toAjax(iWmStoreProductService.insertAndupdateByBo(bo));
    }

    /**
     * 修改商品
     */
    @SaCheckPermission("product:storeProduct:edit")
    @Log(title = "商品", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody StoreProductBo bo) {
        return toAjax(iWmStoreProductService.insertAndupdateByBo(bo));
    }

    /**
     * 删除商品
     *
     * @param ids 主键串
     */
    @SaCheckPermission("product:storeProduct:remove")
    @Log(title = "商品", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iWmStoreProductService.deleteWithValidByIds(Arrays.asList(ids), true));
    }

    /**
     * 商品上架/下架
     *
     * @param id 主键串
     */
    @CacheEvict(cacheNames = ShopConstants.WMHOP_REDIS_INDEX_KEY,allEntries = true)
    @PostMapping(value = "/onsale/{id}")
    public R<Void> onSale(@PathVariable Long id,@RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        iWmStoreProductService.onSale(id,jsonObject.getInteger("status"));
        return R.ok();
    }

    /**
     * 生成属性
     */
    @PostMapping(value = "/isFormatAttr/{id}")
    public R<Map<String, Object>> formatAttr(@PathVariable Long id,@RequestBody String jsonStr){
        return R.ok(iWmStoreProductService.getFormatAttr(id,jsonStr,false));
    }

    /**
     * 授权商品
     */
    @PostMapping(value = "/batchAuthorizeGoods")
    public R<Boolean> batchAuthorizeGoods(@RequestBody StoreProductBo bo) {
      return R.ok(iWmStoreProductService.batchAuthorizeGoods(bo));
    }

    /**
     * 获取树形商品
     */
    @PostMapping(value = "/getProductBigTree")
    public R<List<ProductTreeNodeVo>> getProductBigTree() {
      return R.ok(iWmStoreProductService.getProductBigTree());
    }

}
