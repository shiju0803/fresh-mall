package com.zzq.web.controller.admin.system.runner;

import com.zzq.common.config.WMShopConfig;
import com.zzq.system.service.ISysConfigService;
import com.zzq.system.service.ISysDictTypeService;
import com.zzq.web.controller.admin.system.service.ISysOssConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化 system 模块对应业务数据
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SystemApplicationRunner implements ApplicationRunner {

    private final WMShopConfig freshMallConfig;
    private final ISysConfigService configService;
    private final ISysDictTypeService dictTypeService;
    private final ISysOssConfigService ossConfigService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ossConfigService.init();
        log.info("初始化OSS配置成功");
        if (freshMallConfig.isCacheLazy()) {
            return;
        }
        configService.loadingConfigCache();
        log.info("加载参数缓存数据成功");
        dictTypeService.loadingDictCache();
        log.info("加载字典缓存数据成功");
    }

}
