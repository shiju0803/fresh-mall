package com.zzq.web.controller.app.user;

import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.PageQuery;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.user.domain.bo.UserBillBo;
import com.zzq.user.domain.vo.UserBillVo;
import com.zzq.web.controller.app.user.service.IAppUserBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户账单
 * @date 2024-02-14
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/app/userBill")
public class AppUserBillController extends BaseAppController {

    private final IAppUserBillService kxAppUserBillService;

    /**
     * 查询用户账单列表
     */
    @GetMapping("/list")
    public TableDataInfo<UserBillVo> list(UserBillBo bo, PageQuery pageQuery) {
        Long userId = getAppLoginUser().getUserId();
        bo.setUid(userId);
        bo.setCategory("integral");
        return kxAppUserBillService.queryPageList(bo, pageQuery);
    }
}
