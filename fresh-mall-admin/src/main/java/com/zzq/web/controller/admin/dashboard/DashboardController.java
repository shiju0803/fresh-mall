package com.zzq.web.controller.admin.dashboard;

import com.zzq.common.core.controller.BaseController;
import com.zzq.common.core.domain.R;
import com.zzq.dashboard.domain.OrderTimeDataDTO;
import com.zzq.dashboard.domain.SalesStatementDTO;
import com.zzq.dashboard.domain.UserStatementDTO;
import com.zzq.web.controller.admin.dashboard.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * 商铺广告
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/dashboard/dashboard")
public class DashboardController extends BaseController {

    private final DashboardService dashboardService;

    /**
     * 用户数量统计
     *
     * @param
     * @return
     */
    @GetMapping("/countUser")
    public R<List<UserStatementDTO>> countUser() {
        return R.ok(dashboardService.countUser());
    }


    /**
     * 首页订单/用户等统计
     * @return OrderTimeDataDto
     */
    @GetMapping("/data/count")
    public R<OrderTimeDataDTO> getCount(Long storageId) {
        return R.ok(dashboardService.getCount(storageId));
    }

    /**
     * 销售统计
     *
     * @param storageId
     * @return
     */
    @GetMapping("/getSalesStatement")
    public R<List<SalesStatementDTO>> getSalesStatement(Long storageId) {
        Set<Long> storagePermission = getLoginUser().getStoragePermission();
        return R.ok(dashboardService.getSalesStatement(storageId,storagePermission));
    }

    /**
     * 近两日销售统计
     *
     * @param storageId
     * @return
     */
    @GetMapping("/getTodayAndYesterdaySales")
    public R<List<SalesStatementDTO>> getTodayAndYesterdaySales(Long storageId) {
        Set<Long> storagePermission = getLoginUser().getStoragePermission();
        return R.ok(dashboardService.getTodayAndYesterdaySales(storageId,storagePermission));
    }

    /**
     * 按小时统计销量
     *
     * @param storageId
     * @return
     */
    @GetMapping("/getSalesByHour")
    public R<List<SalesStatementDTO>> getSalesByHour(Long storageId) {
        Set<Long> storagePermission = getLoginUser().getStoragePermission();
        return R.ok(dashboardService.getSalesByHour(storageId,storagePermission));
    }
}
