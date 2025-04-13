package com.zzq.web.controller.admin.basic;

import com.zzq.common.core.controller.BaseController;
import com.zzq.common.core.domain.R;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础信息
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/basic")
public class BasicController extends BaseController {


    private final static String MAP_KEY = "mapKey";

    @Value("${com.zzq.address.map.key}")
    private String geocodeKey;

    /**
     * 查询基础信息
     */
    @GetMapping("/queryBasic")
    public R<String> queryBasic(String key) {
        if (MAP_KEY.equals(key)) {
            return R.ok(geocodeKey);
        }
        return R.fail("不存在该key的值");
    }


}
