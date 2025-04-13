package com.zzq.web.controller.app.recommend;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.ObjectUtil;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.domain.entity.SysDictData;
import com.zzq.web.controller.app.recommend.service.IAppRecommendService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * app推荐类
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/recommend/app")
public class AppRecommendController extends BaseAppController {

    private final IAppRecommendService recommendService;

    /**
     * 获取推荐枚举
     */
    @SaIgnore
    @GetMapping("/getRecommendTypeEnums")
    public R<List<SysDictData>> dictType() {
        List<SysDictData> data = recommendService.getRecommendTypeEnums();
        if (ObjectUtil.isNull(data)) {
            data = new ArrayList<>();
        }
        return R.ok(data);
    }
}
