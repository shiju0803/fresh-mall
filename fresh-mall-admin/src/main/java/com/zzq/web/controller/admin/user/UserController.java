package com.zzq.web.controller.admin.user;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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
import com.zzq.user.domain.bo.UserBo;
import com.zzq.user.domain.vo.PromUserVo;
import com.zzq.user.domain.vo.UserVo;
import com.zzq.web.controller.admin.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

/**
 * 用户
 * @date 2023-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/user")
public class UserController extends BaseController {

    private final IUserService iWmUserService;

    /**
     * 查询用户列表
     */
    @SaCheckPermission("user:user:list")
    @GetMapping("/list")
    public TableDataInfo<UserVo> list(UserBo bo, PageQuery pageQuery) {
        if ("nickname".equals(bo.getType())) {
            bo.setNickname(bo.getValue());
        }
        if ("phone".equals(bo.getType())) {
            bo.setPhone(bo.getValue());
        }
        return iWmUserService.queryPageList(bo, pageQuery);
    }

    /**
     * 导出用户列表
     */
    @SaCheckPermission("user:user:export")
    @Log(title = "用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(UserBo bo, HttpServletResponse response) {
        List<UserVo> list = iWmUserService.queryList(bo);
        ExcelUtil.exportExcel(list, "用户", UserVo.class, response);
    }

    /**
     * 获取用户详细信息
     *
     * @param uid 主键
     */
    @SaCheckPermission("user:user:query")
    @GetMapping("/{uid}")
    public R<UserVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long uid) {
        return R.ok(iWmUserService.queryById(uid));
    }

    /**
     * 新增用户
     */
    @SaCheckPermission("user:user:add")
    @Log(title = "用户", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody UserBo bo) {
        return toAjax(iWmUserService.insertByBo(bo));
    }

    /**
     * 修改用户
     */
    @SaCheckPermission("user:user:edit")
    @Log(title = "用户", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody UserBo bo) {
        return toAjax(iWmUserService.updateByBo(bo));
    }

    /**
     * 删除用户
     *
     * @param uids 主键串
     */
    @SaCheckPermission("user:user:remove")
    @Log(title = "用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{uids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] uids) {
        return toAjax(iWmUserService.deleteWithValidByIds(Arrays.asList(uids), true));
    }


    /**
     * 查看下级
     * @return
     */
    @PostMapping(value = "/spread")
    @SaCheckPermission("user:user:query")
    public R<List<PromUserVo>> getSpread(@RequestBody UserBo bo){
        return R.ok(iWmUserService.querySpread(bo));
    }



    /**
     * 更改状态
     */
    @SaCheckPermission("user:user:edit")
    @PostMapping(value = "/onStatus/{id}")
    public R<Void> onStatus(@PathVariable Long id, @RequestBody String jsonStr){
        JSONObject jsonObject = JSON.parseObject(jsonStr);
        Integer status = jsonObject.getInteger("status");
        iWmUserService.onStatus(id,status);
        return R.ok();
    }

    /**
     * 修改余额
     */
    @RepeatSubmit()
    @PostMapping(value = "/money")
    @SaCheckPermission("user:user:edit")
    public R<Void> updatePrice(@Validated @RequestBody UserBo param){
        iWmUserService.updateMoney(param);
        return R.ok();
    }

}
