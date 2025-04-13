package com.zzq.web.controller.admin.carousel;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.zzq.carousel.domain.bo.CarouselBo;
import com.zzq.carousel.domain.vo.CarouselVo;
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
import com.zzq.web.controller.admin.carousel.service.ICarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 商铺广告
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/carousel/carousel")
public class CarouselController extends BaseController {

    private final ICarouselService iCarouselService;

    /**
     * 查询商铺广告列表
     */
    @SaCheckPermission("carousel:carousel:list")
    @GetMapping("/list")
    public TableDataInfo<CarouselVo> list(CarouselBo bo, PageQuery pageQuery) {
        return iCarouselService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出商铺广告列表
     */
    @SaCheckPermission("carousel:carousel:export")
    @Log(title = "商铺广告", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(CarouselBo bo, HttpServletResponse response) {
        List<CarouselVo> list = iCarouselService.queryList(bo);
        ExcelUtil.exportExcel(list, "商铺广告", CarouselVo.class, response);
    }

    /**
     * 获取商铺广告详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("carousel:carousel:query")
    @GetMapping("/{id}")
    public R<CarouselVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(iCarouselService.queryById(id));
    }

    /**
     * 新增商铺广告
     */
    @SaCheckPermission("carousel:carousel:add")
    @Log(title = "商铺广告", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody CarouselBo bo) {
        return toAjax(iCarouselService.insertByBo(bo));
    }

    /**
     * 修改商铺广告
     */
    @SaCheckPermission("carousel:carousel:edit")
    @Log(title = "商铺广告", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody CarouselBo bo) {
        return toAjax(iCarouselService.updateByBo(bo));
    }

    /**
     * 删除商铺广告
     *
     * @param ids 主键串
     */
    @SaCheckPermission("carousel:carousel:remove")
    @Log(title = "商铺广告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iCarouselService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
