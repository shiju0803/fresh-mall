package com.zzq.web.controller.app.notice;

import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.system.domain.SysNotice;
import com.zzq.web.controller.app.notice.service.INoticetService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 公告
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/notice/app")
public class NoticeController extends BaseAppController {

    private final INoticetService noticetService;

    /**
     * 查询客户收藏列表
     */
    @GetMapping("/list")
    public R<List<SysNotice>> list(SysNotice bo) {
        return R.ok(noticetService.queryList(bo));
    }
}
