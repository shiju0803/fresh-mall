package com.zzq.web.controller.admin.recommend;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzq.carousel.domain.Carousel;
import com.zzq.carousel.domain.vo.CarouselVo;
import com.zzq.carousel.mapper.CarouselMapper;
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
import com.zzq.recommend.domain.bo.RecommendBo;
import com.zzq.recommend.domain.vo.RecommendVo;
import com.zzq.web.controller.admin.recommend.service.IRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 推荐管理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/recommend/recommend")
public class RecommendController extends BaseController {

    private final IRecommendService iRecommendService;

    private final CarouselMapper baseMapper;

    /**
     * 查询推荐管理列表
     */
    @SaCheckPermission("recommend:recommend:list")
    @GetMapping("/list")
    public TableDataInfo<RecommendVo> list(RecommendBo bo, PageQuery pageQuery) {
        return iRecommendService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询推荐管理列表(全部)
     */
    @GetMapping("/listAll")
    public R<List<RecommendVo>> listAll(RecommendBo bo) {
        return R.ok(iRecommendService.queryList(bo));
    }

    /**
     * 导出推荐管理列表
     */
    @SaCheckPermission("recommend:recommend:export")
    @Log(title = "推荐管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(RecommendBo bo, HttpServletResponse response) {
        List<RecommendVo> list = iRecommendService.queryList(bo);
        ExcelUtil.exportExcel(list, "推荐管理", RecommendVo.class, response);
    }

    /**
     * 获取推荐管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("recommend:recommend:query")
    @GetMapping("/{id}")
    public R<RecommendVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(iRecommendService.queryById(id));
    }
    /**
     * 获取推荐类型信息
     */
    @GetMapping("/listRecommendType")
    public R<List<CarouselVo>> listRecommendType() {
        List<CarouselVo> carouselVos = baseMapper.selectVoList(new LambdaQueryWrapper<Carousel>().eq(Carousel::getAdType, 4));
        return R.ok(carouselVos.stream()
                .filter(vo -> vo.getUrl().contains("/pages/parity/parity"))
                .collect(Collectors.toList()));
    }

    /**
     * 新增推荐管理
     */
    @SaCheckPermission("recommend:recommend:add")
    @Log(title = "推荐管理", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody RecommendBo bo) {
        return toAjax(iRecommendService.insertByBo(bo));
    }

    /**
     * 批量新增推荐管理
     */
    @PostMapping("/addRecommendBatch")
    public R<Void> addRecommendBatch(@Validated(AddGroup.class) @RequestBody RecommendBo bo) {
        return toAjax(iRecommendService.addRecommendBatch(bo));
    }

    /**
     * 修改推荐管理
     */
    @SaCheckPermission("recommend:recommend:edit")
    @Log(title = "推荐管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody RecommendBo bo) {
        return toAjax(iRecommendService.updateByBo(bo));
    }

    /**
     * 删除推荐管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("recommend:recommend:remove")
    @Log(title = "推荐管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iRecommendService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
