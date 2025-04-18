package com.zzq.web.controller.admin.system.service;

import cn.dev33.satoken.secure.BCrypt;
import com.zzq.common.constant.CacheConstants;
import com.zzq.common.constant.Constants;
import com.zzq.common.constant.UserConstants;
import com.zzq.common.core.domain.entity.SysUser;
import com.zzq.common.core.domain.event.LogininforEvent;
import com.zzq.common.core.domain.model.RegisterBody;
import com.zzq.common.enums.UserType;
import com.zzq.common.exception.user.CaptchaException;
import com.zzq.common.exception.user.CaptchaExpireException;
import com.zzq.common.exception.user.UserException;
import com.zzq.common.utils.MessageUtils;
import com.zzq.common.utils.ServletUtils;
import com.zzq.common.utils.StringUtils;
import com.zzq.common.utils.redis.RedisUtils;
import com.zzq.common.utils.spring.SpringUtils;
import com.zzq.system.service.ISysConfigService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 注册校验方法
 */
@RequiredArgsConstructor
@Service
public class SysRegisterService {

    private final ISysUserService userService;
    private final ISysConfigService configService;

    /**
     * 注册
     */
    public void register(RegisterBody registerBody) {
        HttpServletRequest request = ServletUtils.getRequest();
        String username = registerBody.getUsername();
        String password = registerBody.getPassword();
        // 校验用户类型是否存在
        String userType = UserType.getUserType(registerBody.getUserType()).getUserType();

        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.getCode(), registerBody.getUuid(), request);
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserName(username);
        sysUser.setNickName(username);
        sysUser.setPassword(BCrypt.hashpw(password));
        sysUser.setUserType(userType);

        if (UserConstants.NOT_UNIQUE.equals(userService.checkUserNameUnique(sysUser))) {
            throw new UserException("user.register.save.error", username);
        }
        boolean regFlag = userService.registerUser(sysUser);
        if (!regFlag) {
            throw new UserException("user.register.error");
        }
        recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.register.success"));
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid, HttpServletRequest request) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.expire"));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLogininfor(username, Constants.REGISTER, MessageUtils.message("user.jcaptcha.error"));
            throw new CaptchaException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    private void recordLogininfor(String username, String status, String message) {
        LogininforEvent logininforEvent = new LogininforEvent();
        logininforEvent.setUsername(username);
        logininforEvent.setStatus(status);
        logininforEvent.setMessage(message);
        logininforEvent.setRequest(ServletUtils.getRequest());
        SpringUtils.context().publishEvent(logininforEvent);
    }

}
