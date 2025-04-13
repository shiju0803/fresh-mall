package com.zzq.web.controller.app.user;

import com.zzq.common.annotation.RepeatSubmit;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserCollectBo;
import com.zzq.user.domain.vo.UserCollectVo;
import com.zzq.web.controller.app.user.service.IUserCollectService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;

/**
 * 客户收藏
 *
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/userCollect")
public class UserCollectController extends BaseAppController {

    private final IUserCollectService iUserCollectService;

    /**
     * 查询客户收藏列表
     */
    @GetMapping("/list")
    public TableDataInfo<UserCollectVo> list(UserCollectBo bo, PageQuery pageQuery) {
        return iUserCollectService.queryPageList(bo, pageQuery);
    }

    /**
     * 查询某一用户收藏记录
     */
    @GetMapping("/getCollectAll")
    public TableDataInfo<UserCollectVo> getCollectAll(UserCollectBo bo, PageQuery pageQuery) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUserId(userId);
        return iUserCollectService.getCollectAll(bo, pageQuery);
    }

    /**
     * 获取客户收藏详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    public R<UserCollectVo> getInfo(@NotNull(message = "主键不能为空")
                                    @PathVariable Long id) {
        return R.ok(iUserCollectService.queryById(id));
    }

    /**
     * 新增客户收藏
     */
    @RepeatSubmit()
    @PostMapping("/addCollect")
    public R<Void> addCollect(@RequestBody UserCollectBo bo) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUserId(userId);
        return toAjax(iUserCollectService.insertByBo(bo));
    }

    /**
     * 删除客户收藏
     */
    @PostMapping("/deleteCollect")
    public R<Void> deleteCollect(@RequestBody UserCollectBo bo) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUserId(userId);
        return toAjax(iUserCollectService.deleteCollect(bo));
    }

    /**
     * 删除客户收藏
     *
     * @param ids 主键串
     */
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(iUserCollectService.deleteWithValidByIds(Arrays.asList(ids), true));
    }
}
