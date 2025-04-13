package com.zzq.web.controller.app.user;

import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.user.domain.bo.UserBo;
import com.zzq.user.domain.vo.UserVo;
import com.zzq.web.controller.app.user.service.IAppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * app端登录
 * @version 1.0
 * @date 2023/9/1
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/app")
public class AppUserController extends BaseAppController {

    private final IAppUserService kxUserService;

    /**
     * 查询某一用户收藏记录
     */
    @GetMapping("/getUser")
    public R<UserVo> getUser() {
        Long userId = getAppLoginUser().getUserId();
        return R.ok(kxUserService.queryById(userId));
    }

    /**
     * 更新
     */
    @PostMapping("/updateUser")
    public R<Boolean> updateUser(@RequestBody UserBo bo) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUid(userId);
        return R.ok(kxUserService.updateByBo(bo));
    }
}
