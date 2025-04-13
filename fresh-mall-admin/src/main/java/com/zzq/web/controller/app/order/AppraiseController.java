package com.zzq.web.controller.app.order;

import com.zzq.common.annotation.RepeatSubmit;
import com.zzq.common.core.controller.BaseAppController;
import com.zzq.common.core.domain.R;
import com.zzq.common.core.page.TableDataInfo;
import com.zzq.order.domain.bo.AppraiseRequestBo;
import com.zzq.order.domain.vo.StoreAppraiseVo;
import com.zzq.web.controller.app.order.service.IAppAppraiseService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 评论
 * @date 2023-04-06
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/appraise/app")
public class AppraiseController extends BaseAppController {

    private final IAppAppraiseService kxAppraiseService;

    /**
     * 增加评论
     */
    @RepeatSubmit()
    @PostMapping("/addAppraise")
    public R<Void> addAppraise(@RequestBody AppraiseRequestBo bo) {
        Long userId = getAppLoginUser().getUserId();
        return toAjax(kxAppraiseService.addAppraise(bo, userId));
    }

    /**
     * 查询商品的所有评论
     *
     * @param productId
     * @return
     */
    @GetMapping("/getSpuAllAppraise")
    public TableDataInfo<StoreAppraiseVo> getSpuAllAppraise(Long productId, Integer pageNo, Integer pageSize) {
        return kxAppraiseService.getProductAppraiseByPage(productId, pageNo, pageSize, 1);
    }


}
