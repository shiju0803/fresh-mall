package com.zzq.common.enums;

import com.zzq.common.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 设备类型
 * 针对一套 用户体系
 */
@Getter
@AllArgsConstructor
public enum DeviceType {

    /**
     * pc端
     */
    PC("pc"),

    /**
     * app端
     */
    APP("app");

    public static DeviceType getDeviceType(String str) {
        for (DeviceType value : values()) {
            if (StringUtils.contains(str, value.getDevice())) {
                return value;
            }
        }
        return null;
        //throw new RuntimeException("'DeviceType' not found By " + str);
    }

    private final String device;
}
