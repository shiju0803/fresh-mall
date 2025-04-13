package com.zzq.web.controller.app.carousel;

import cn.dev33.satoken.annotation.SaIgnore;
import com.zzq.carousel.domain.Carousel;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.web.controller.app.carousel.service.IAppCarouselService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * app推广
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/carousel/app")
public class AppCarouselController extends BaseAppController {

    private final IAppCarouselService appCarouselService;

    /**
     * 取得活跃广告
     *
     * @param adType
     */
    @SaIgnore
    @GetMapping("/getCarouselActive")
    public R<List<Carousel>> getCarouselActive(@NotNull(message = "type不能为空") Integer adType) {
        return R.ok(appCarouselService.listAll(adType));
    }
}
